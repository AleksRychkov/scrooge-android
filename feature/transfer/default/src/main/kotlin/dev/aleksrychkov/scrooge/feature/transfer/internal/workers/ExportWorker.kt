package dev.aleksrychkov.scrooge.feature.transfer.internal.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.TransferStateEntity
import dev.aleksrychkov.scrooge.feature.transfer.SetTransferStateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class ExportWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    companion object {
        const val KEY_URI = "export_uri"
    }

    private val setTransferStateUseCase: Lazy<SetTransferStateUseCase> = getLazy()

    @Suppress("TooGenericExceptionCaught")
    override suspend fun doWork(): Result = withContext(Dispatchers.Default) {
        val stateUseCase = setTransferStateUseCase.value
        try {
            stateUseCase(TransferStateEntity(TransferStateEntity.State.Exporting()))

            val uri = requireNotNull(inputData.getString(KEY_URI))
            export(uri)

            stateUseCase(TransferStateEntity(TransferStateEntity.State.ExportingSuccess(info = uri)))
            Result.success()
        } catch (e: Exception) {
            stateUseCase(TransferStateEntity(TransferStateEntity.State.ExportingFailed(info = e.message)))
            Result.failure()
        }
    }

    private suspend fun export(uriString: String) {
        println(uriString)
    }
}
