package dev.aleksrychkov.scrooge.feature.transfer.internal

import dev.aleksrychkov.scrooge.core.entity.TransferStateEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.transfer.SetTransferStateUseCase
import dev.aleksrychkov.scrooge.feature.transfer.internal.data.repository.TransferStateRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultSetTransferStateUseCase(
    private val repository: Lazy<TransferStateRepository>,
    private val ioDispatcher: CoroutineDispatcher,
) : SetTransferStateUseCase {
    override suspend fun invoke(state: TransferStateEntity): Unit = withContext(ioDispatcher) {
        runSuspendCatching {
            repository.value.set(state)
        }
    }
}
