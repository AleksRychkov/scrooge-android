package dev.aleksrychkov.scrooge.feature.transfer

import dev.aleksrychkov.scrooge.core.entity.TransferStateEntity

fun interface SetTransferStateUseCase {
    suspend operator fun invoke(state: TransferStateEntity)
}
