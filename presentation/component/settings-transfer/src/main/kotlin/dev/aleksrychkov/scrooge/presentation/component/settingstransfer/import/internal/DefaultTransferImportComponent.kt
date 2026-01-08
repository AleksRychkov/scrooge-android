package dev.aleksrychkov.scrooge.presentation.component.settingstransfer.import.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.udfextensions.retainedCoroutineScope
import dev.aleksrychkov.scrooge.feature.transfer.ImportDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class DefaultTransferImportComponent(
    componentContext: ComponentContext
) : TransferImportComponentInternal, ComponentContext by componentContext {

    private val importUseCase: Lazy<ImportDataUseCase> = getLazy()

    override fun import() {
        retainedCoroutineScope().launch(Dispatchers.IO) {
            try {
                importUseCase.value.invoke()
            } catch (_: IllegalStateException) {
                // todo: handle it?
            }
        }
    }
}
