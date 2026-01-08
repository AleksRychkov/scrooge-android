package dev.aleksrychkov.scrooge.presentation.component.roottransfer.internal.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.TransferStateEntity

@Immutable
internal data class RootTransferState(
    val state: TransferStateEntity = TransferStateEntity(),
)
