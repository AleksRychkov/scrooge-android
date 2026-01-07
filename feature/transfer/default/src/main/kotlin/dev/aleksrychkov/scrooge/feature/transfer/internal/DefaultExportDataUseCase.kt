@file:Suppress("UnusedPrivateProperty")

package dev.aleksrychkov.scrooge.feature.transfer.internal

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import dev.aleksrychkov.scrooge.feature.transfer.ExportDataUseCase
import dev.aleksrychkov.scrooge.feature.transfer.GetExportUriUseCase
import dev.aleksrychkov.scrooge.feature.transfer.internal.workers.ExportWorker

internal class DefaultExportDataUseCase(
    private val exportUriUseCase: Lazy<GetExportUriUseCase>,
    private val context: Context,
) : ExportDataUseCase {

    private companion object {
        const val DB_BACKUP_NAME = "scrooge_backup.db"
    }

    override suspend fun invoke() {
        val uriString = exportUriUseCase.value.invoke(DB_BACKUP_NAME)
        checkNotNull(uriString)
        val data = Data.Builder().put(ExportWorker.KEY_URI, uriString).build()
        val request = OneTimeWorkRequestBuilder<ExportWorker>()
            .setInputData(data)
            .setExpedited(policy = OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()
        WorkManager.getInstance(context).enqueue(request)
    }
}
