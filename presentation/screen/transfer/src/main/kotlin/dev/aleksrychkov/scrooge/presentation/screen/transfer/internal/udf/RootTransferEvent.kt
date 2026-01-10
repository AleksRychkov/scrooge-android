package dev.aleksrychkov.scrooge.presentation.screen.transfer.internal.udf

import dev.aleksrychkov.scrooge.core.entity.TransferStateEntity

internal sealed interface RootTransferEvent {
    sealed interface External : RootTransferEvent {
        data object ObserveState : RootTransferEvent
        data class SetState(val state: TransferStateEntity.State) : RootTransferEvent
    }

    sealed interface Internal : RootTransferEvent {
        data class ObserveResult(val state: TransferStateEntity) : Internal
    }
}
