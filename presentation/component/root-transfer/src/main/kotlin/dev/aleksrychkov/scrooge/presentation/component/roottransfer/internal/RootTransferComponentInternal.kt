package dev.aleksrychkov.scrooge.presentation.component.roottransfer.internal

import dev.aleksrychkov.scrooge.core.entity.TransferStateEntity
import dev.aleksrychkov.scrooge.presentation.component.roottransfer.RootTransferComponent
import dev.aleksrychkov.scrooge.presentation.component.roottransfer.internal.udf.RootTransferState
import kotlinx.coroutines.flow.StateFlow

internal interface RootTransferComponentInternal : RootTransferComponent {
    val state: StateFlow<RootTransferState>

    fun setState(state: TransferStateEntity.State)
}
