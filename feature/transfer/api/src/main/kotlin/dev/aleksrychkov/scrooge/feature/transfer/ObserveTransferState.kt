package dev.aleksrychkov.scrooge.feature.transfer

import dev.aleksrychkov.scrooge.core.entity.TransferStateEntity
import kotlinx.coroutines.flow.Flow

fun interface ObserveTransferState {
    operator fun invoke(): Flow<TransferStateEntity>
}
