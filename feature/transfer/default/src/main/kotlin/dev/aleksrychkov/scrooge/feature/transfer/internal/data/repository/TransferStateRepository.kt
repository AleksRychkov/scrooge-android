package dev.aleksrychkov.scrooge.feature.transfer.internal.data.repository

import dev.aleksrychkov.scrooge.core.entity.TransferStateEntity
import dev.aleksrychkov.scrooge.feature.transfer.internal.data.source.TransferStateSource
import kotlinx.coroutines.flow.Flow

internal interface TransferStateRepository {
    companion object {
        operator fun invoke(
            source: TransferStateSource
        ): TransferStateRepository =
            DefaultTransferStateRepository(source = source)
    }

    suspend fun observe(): Flow<TransferStateEntity>
    suspend fun set(state: TransferStateEntity)
}

private class DefaultTransferStateRepository(
    private val source: TransferStateSource,
) : TransferStateRepository {
    override suspend fun observe(): Flow<TransferStateEntity> =
        source.observe()

    override suspend fun set(state: TransferStateEntity) {
        source.set(state)
    }
}
