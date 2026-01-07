package dev.aleksrychkov.scrooge.presentation.component.settingstransfer.export

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.presentation.component.settingstransfer.export.internal.DefaultTransferExportComponent

interface TransferExportComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext
        ): TransferExportComponent {
            return DefaultTransferExportComponent(componentContext = componentContext)
        }
    }
}
