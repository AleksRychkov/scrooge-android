package dev.aleksrychkov.scrooge.feature.transfer.internal

import dev.aleksrychkov.scrooge.core.entity.TransferStateEntity
import dev.aleksrychkov.scrooge.feature.transfer.ObserveTransferStateUseCase
import dev.aleksrychkov.scrooge.feature.transfer.internal.data.repository.TransferStateRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

internal class DefaultObserveTransferStateUseCase(
    private val repository: Lazy<TransferStateRepository>,
    private val ioDispatcher: CoroutineDispatcher,
) : ObserveTransferStateUseCase {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(): Flow<TransferStateEntity> {
        return flowOf(Unit)
            .flatMapConcat { repository.value.observe() }
            .flowOn(ioDispatcher)
            .catch { emit(TransferStateEntity()) }
    }
}
