package dev.aleksrychkov.scrooge.presentation.screen.transfer.internal.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.TransferStateEntity

@Immutable
internal data class RootTransferState(
    val state: TransferStateEntity = TransferStateEntity(),
)
