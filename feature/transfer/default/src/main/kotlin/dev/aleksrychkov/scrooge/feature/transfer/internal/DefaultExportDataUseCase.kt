@file:Suppress("UnusedPrivateProperty")

package dev.aleksrychkov.scrooge.feature.transfer.internal

import android.content.Context
import android.net.Uri
import dev.aleksrychkov.scrooge.feature.transfer.ExportDataUseCase
import dev.aleksrychkov.scrooge.feature.transfer.GetExportUriUseCase

internal class DefaultExportDataUseCase(
    private val exportUriUseCase: Lazy<GetExportUriUseCase>,
    private val context: Context,
) : ExportDataUseCase {

    private companion object {
        const val DB_BACKUP_NAME = "scrooge_backup.db"
    }

    override suspend fun invoke() {
        val uriString = exportUriUseCase.value.invoke(DB_BACKUP_NAME)
        val uri = uriString.toUri()
    }

    private fun String?.toUri(): Uri {
        checkNotNull(this)
        return Uri.parse(this)
    }
}
