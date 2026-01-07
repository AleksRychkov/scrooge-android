package dev.aleksrychkov.scrooge.presentation.component.settingstransfer.import.internal

import com.arkivanov.decompose.ComponentContext

internal class DefaultTransferImportComponent(
    componentContext: ComponentContext
) : TransferImportComponentInternal, ComponentContext by componentContext {
    override fun import() {
        // no-op
    }
}
