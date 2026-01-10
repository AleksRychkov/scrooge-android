package dev.aleksrychkov.scrooge.presentation.screen.transfer.internal

import dev.aleksrychkov.scrooge.core.entity.TransferStateEntity
import dev.aleksrychkov.scrooge.presentation.screen.transfer.RootTransferComponent
import dev.aleksrychkov.scrooge.presentation.screen.transfer.internal.udf.RootTransferState
import kotlinx.coroutines.flow.StateFlow

internal interface RootTransferComponentInternal : RootTransferComponent {
    val state: StateFlow<RootTransferState>

    fun setState(state: TransferStateEntity.State)
}
