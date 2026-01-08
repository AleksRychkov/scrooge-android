package dev.aleksrychkov.scrooge.presentation.component.roottransfer.internal.udf

import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.presentation.component.roottransfer.internal.udf.actors.ObserveDelegate
import dev.aleksrychkov.scrooge.presentation.component.roottransfer.internal.udf.actors.SetStateDelegate
import kotlinx.coroutines.flow.Flow

internal class RootTransferActor(
    private val observeDelegate: ObserveDelegate = ObserveDelegate(),
    private val setStateDelegate: SetStateDelegate = SetStateDelegate(),
) : Actor<RootTransferCommand, RootTransferEvent> {
    override suspend fun process(command: RootTransferCommand): Flow<RootTransferEvent> {
        return when (command) {
            RootTransferCommand.ObserveState -> observeDelegate.invoke()
            is RootTransferCommand.SetState -> setStateDelegate.invoke(command)
        }
    }
}
