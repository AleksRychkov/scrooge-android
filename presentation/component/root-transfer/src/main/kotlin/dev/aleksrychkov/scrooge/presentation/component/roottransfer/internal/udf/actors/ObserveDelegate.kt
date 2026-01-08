package dev.aleksrychkov.scrooge.presentation.component.roottransfer.internal.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.feature.transfer.ObserveTransferStateUseCase
import dev.aleksrychkov.scrooge.presentation.component.roottransfer.internal.udf.RootTransferEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ObserveDelegate(
    private val useCase: Lazy<ObserveTransferStateUseCase> = getLazy(),
) {
    fun invoke(): Flow<RootTransferEvent> {
        return useCase.value.invoke().map { RootTransferEvent.Internal.ObserveResult(state = it) }
    }
}
