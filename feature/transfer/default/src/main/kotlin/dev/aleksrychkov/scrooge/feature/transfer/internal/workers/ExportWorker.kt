package dev.aleksrychkov.scrooge.feature.transfer.internal.workers

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dev.aleksrychkov.scrooge.core.database.fileadapter.DatabaseFileAdapter
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.TransferStateEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.transfer.SetTransferStateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedOutputStream
import java.io.DataOutputStream
import java.io.OutputStream

internal class ExportWorker(
    private val context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    companion object {
        private const val KEY_URI = "export_uri"

        fun fire(context: Context, uri: String) {
            val data = Data.Builder().putString(KEY_URI, uri).build()
            val request = OneTimeWorkRequestBuilder<ExportWorker>()
                .setInputData(data)
                .setExpedited(policy = OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()
            WorkManager.getInstance(context).enqueue(request)
        }
    }

    private val setTransferStateUseCase: Lazy<SetTransferStateUseCase> = getLazy()
    private val databaseFileAdapter: Lazy<DatabaseFileAdapter> = getLazy()

    @Suppress("TooGenericExceptionCaught")
    override suspend fun doWork(): Result = withContext(Dispatchers.Default) {
        val stateUseCase = setTransferStateUseCase.value
        runSuspendCatching {
            stateUseCase(TransferStateEntity(TransferStateEntity.State.Exporting()))

            val uri = requireNotNull(inputData.getString(KEY_URI)).let(Uri::parse)
            export(uri)

            stateUseCase(TransferStateEntity(TransferStateEntity.State.ExportingSuccess(info = uri.toString())))
            Result.success()
        }
            .onFailure { e ->
                cleanOnFailure()
                e.printStackTrace()
                stateUseCase(TransferStateEntity(TransferStateEntity.State.ExportingFailed(info = e.message)))
            }
            .getOrDefault(Result.failure())
    }

    @SuppressLint("UseKtx")
    private suspend fun export(uri: Uri) {
        val outputStream: OutputStream =
            requireNotNull(context.contentResolver.openOutputStream(uri))
        DataOutputStream(BufferedOutputStream(outputStream)).use {
            databaseFileAdapter.value.write(it)
        }
    }

    private fun cleanOnFailure() {
        runSuspendCatching {
            val uri = requireNotNull(inputData.getString(KEY_URI)).let(Uri::parse)
            DocumentsContract.deleteDocument(context.contentResolver, uri)
        }
    }
}
