package dev.aleksrychkov.scrooge.feature.transfer.internal.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.aleksrychkov.scrooge.core.database.DatabaseManger
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.TransferStateEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.transfer.SetTransferStateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

internal class ExportWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    companion object {
        const val KEY_URI = "export_uri"
    }

    private val setTransferStateUseCase: Lazy<SetTransferStateUseCase> = getLazy()
    private val databaseManger: Lazy<DatabaseManger> = getLazy()

    @Suppress("TooGenericExceptionCaught")
    override suspend fun doWork(): Result = withContext(Dispatchers.Default) {
        val stateUseCase = setTransferStateUseCase.value
        runSuspendCatching {
            stateUseCase(TransferStateEntity(TransferStateEntity.State.Exporting()))

            val uri = requireNotNull(inputData.getString(KEY_URI))
            export(uri)

            stateUseCase(TransferStateEntity(TransferStateEntity.State.ExportingSuccess(info = uri)))
            Result.success()
        }
            .onFailure { e ->
                stateUseCase(TransferStateEntity(TransferStateEntity.State.ExportingFailed(info = e.message)))
            }
            .getOrDefault(Result.failure())
    }

    @Suppress("MagicNumber", "UnusedParameter")
    private suspend fun export(uriString: String) {
        try {
            databaseManger.value.close()
            delay(2000)
        } finally {
            databaseManger.value.open()
        }
    }
}
