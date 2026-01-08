package dev.aleksrychkov.scrooge.feature.transfer.internal.workers

import android.content.Context
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dev.aleksrychkov.scrooge.core.database.DatabaseManger
import dev.aleksrychkov.scrooge.core.database.fileadapter.DatabaseFileAdapter
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.TransferStateEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.transfer.SetTransferStateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.InputStream

internal class ImportWorker(
    private val context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    companion object {
        const val KEY_URI = "import_uri"

        fun fire(context: Context, uri: String) {
            val data = Data.Builder().put(KEY_URI, uri).build()
            val request = OneTimeWorkRequestBuilder<ImportWorker>()
                .setInputData(data)
                .setExpedited(policy = OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()
            WorkManager.getInstance(context).enqueue(request)
        }
    }

    private val setTransferStateUseCase: Lazy<SetTransferStateUseCase> = getLazy()
    private val databaseFileAdapter: Lazy<DatabaseFileAdapter> = getLazy()
    private val databaseManager: Lazy<DatabaseManger> = getLazy()

    @Suppress("TooGenericExceptionCaught")
    override suspend fun doWork(): Result = withContext(Dispatchers.Default) {
        val stateUseCase = setTransferStateUseCase.value
        runSuspendCatching {
            stateUseCase(TransferStateEntity(TransferStateEntity.State.Importing()))

            databaseManager.value.cleanup()

            val uri = requireNotNull(inputData.getString(KEY_URI))
            import(uri)

            stateUseCase(TransferStateEntity(TransferStateEntity.State.ImportingSuccess(info = uri)))
            Result.success()
        }
            .onFailure { e ->
                e.printStackTrace()
                stateUseCase(TransferStateEntity(TransferStateEntity.State.ImportingFailed(info = e.message)))
            }
            .getOrDefault(Result.failure())
    }

    private suspend fun import(uriString: String) {
        val inputStream: InputStream
        val uri = Uri.parse(uriString)
        inputStream = requireNotNull(context.contentResolver.openInputStream(uri))
        DataInputStream(BufferedInputStream(inputStream)).use {
            databaseFileAdapter.value.read(it)
        }
    }
}
