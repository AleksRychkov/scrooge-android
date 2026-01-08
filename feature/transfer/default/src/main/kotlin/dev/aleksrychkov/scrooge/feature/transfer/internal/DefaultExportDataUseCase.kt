@file:Suppress("UnusedPrivateProperty")

package dev.aleksrychkov.scrooge.feature.transfer.internal

import android.content.Context
import dev.aleksrychkov.scrooge.feature.transfer.ExportDataUseCase
import dev.aleksrychkov.scrooge.feature.transfer.GetExportUriUseCase
import dev.aleksrychkov.scrooge.feature.transfer.internal.workers.ExportWorker
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

internal class DefaultExportDataUseCase(
    private val exportUriUseCase: Lazy<GetExportUriUseCase>,
    private val context: Context,
) : ExportDataUseCase {

    private companion object {
        const val DB_BACKUP_NAME = "scrooge_backup"
    }

    override suspend fun invoke() {
        val dt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString()
        val dbName = DB_BACKUP_NAME + "_" + dt
        val uriString = exportUriUseCase.value.invoke(dbName) ?: return
        ExportWorker.fire(context = context, uri = uriString)
    }
}
