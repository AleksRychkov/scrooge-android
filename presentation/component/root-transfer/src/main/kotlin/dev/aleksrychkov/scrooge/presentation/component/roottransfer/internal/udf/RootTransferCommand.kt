package dev.aleksrychkov.scrooge.presentation.component.roottransfer.internal.udf

import dev.aleksrychkov.scrooge.core.entity.TransferStateEntity

internal sealed interface RootTransferCommand {
    data object ObserveState : RootTransferCommand
    data class SetState(val state: TransferStateEntity.State) : RootTransferCommand
}
