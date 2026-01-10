package dev.aleksrychkov.scrooge.presentation.screen.transfer

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.TransferStateEntity
import dev.aleksrychkov.scrooge.presentation.screen.transfer.internal.DefaultRootTransferComponent

interface RootTransferComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
            state: TransferStateEntity,
        ): RootTransferComponent = DefaultRootTransferComponent(
            componentContext = componentContext,
            state = state,
        )
    }
}
