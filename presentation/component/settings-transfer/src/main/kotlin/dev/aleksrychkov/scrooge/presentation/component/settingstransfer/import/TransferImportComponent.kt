package dev.aleksrychkov.scrooge.presentation.component.settingstransfer.import

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.presentation.component.settingstransfer.import.internal.DefaultTransferImportComponent

interface TransferImportComponent {
    companion object Companion {
        operator fun invoke(
            componentContext: ComponentContext
        ): TransferImportComponent {
            return DefaultTransferImportComponent(componentContext = componentContext)
        }
    }
}
