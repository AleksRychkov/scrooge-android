package dev.aleksrychkov.scrooge.component.transactionform.internal.udf.actors

import dev.aleksrychkov.scrooge.component.transactionform.internal.udf.FormCommand
import dev.aleksrychkov.scrooge.component.transactionform.internal.udf.FormEvent
import dev.aleksrychkov.scrooge.feature.transaction.DeleteTransactionUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class DeleteDelegate(
    private val useCase: Lazy<DeleteTransactionUseCase>,
) {
    suspend operator fun invoke(cmd: FormCommand.Delete): Flow<FormEvent> {
        val state = cmd.state
        if (state.transactionId == null) {
            return flowOf(FormEvent.Internal.DeleteTransactionFailure)
        }
        useCase.value.invoke(transactionId = cmd.state.transactionId)
        return flowOf(FormEvent.Internal.DeleteTransactionSuccess)
    }
}
