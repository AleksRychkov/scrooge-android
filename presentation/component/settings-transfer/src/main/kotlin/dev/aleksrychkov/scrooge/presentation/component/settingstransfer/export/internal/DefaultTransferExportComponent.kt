package dev.aleksrychkov.scrooge.presentation.component.settingstransfer.export.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.udfextensions.retainedCoroutineScope
import dev.aleksrychkov.scrooge.feature.transfer.ExportDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class DefaultTransferExportComponent(
    componentContext: ComponentContext,
) : TransferExportComponentInternal, ComponentContext by componentContext {

    private val exportUseCase: Lazy<ExportDataUseCase> = getLazy()

    @Suppress("SwallowedException")
    override fun export() {
        retainedCoroutineScope().launch(Dispatchers.IO) {
            try {
                exportUseCase.value.invoke()
            } catch (_: IllegalStateException) {
                // todo: handle it?
            }
        }
    }
}
