package dev.aleksrychkov.scrooge.feature.transfer.internal

import android.content.Context
import dev.aleksrychkov.scrooge.feature.transfer.GetImportUriUseCase
import dev.aleksrychkov.scrooge.feature.transfer.ImportDataUseCase
import dev.aleksrychkov.scrooge.feature.transfer.internal.workers.ImportWorker

internal class DefaultImportDataUseCase(
    private val importUriUseCase: Lazy<GetImportUriUseCase>,
    private val context: Context,
) : ImportDataUseCase {

    override suspend fun invoke() {
        val uriString = importUriUseCase.value.invoke()
        checkNotNull(uriString)
        ImportWorker.fire(context = context, uri = uriString)
    }
}
