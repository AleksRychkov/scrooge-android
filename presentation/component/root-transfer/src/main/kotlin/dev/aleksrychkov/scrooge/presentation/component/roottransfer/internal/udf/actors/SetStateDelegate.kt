package dev.aleksrychkov.scrooge.presentation.component.roottransfer.internal.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.TransferStateEntity
import dev.aleksrychkov.scrooge.feature.transfer.SetTransferStateUseCase
import dev.aleksrychkov.scrooge.presentation.component.roottransfer.internal.udf.RootTransferCommand
import dev.aleksrychkov.scrooge.presentation.component.roottransfer.internal.udf.RootTransferEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal class SetStateDelegate(
    private val useCase: Lazy<SetTransferStateUseCase> = getLazy(),
) {
    suspend operator fun invoke(cmd: RootTransferCommand.SetState): Flow<RootTransferEvent> {
        useCase.value.invoke(state = TransferStateEntity(current = cmd.state))
        return emptyFlow()
    }
}
