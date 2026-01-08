package dev.aleksrychkov.scrooge.presentation.component.roottransfer

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.TransferStateEntity
import dev.aleksrychkov.scrooge.presentation.component.roottransfer.internal.DefaultRootTransferComponent

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
