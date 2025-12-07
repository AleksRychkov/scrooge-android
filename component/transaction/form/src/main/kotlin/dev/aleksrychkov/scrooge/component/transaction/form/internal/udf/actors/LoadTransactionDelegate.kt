package dev.aleksrychkov.scrooge.component.transaction.form.internal.udf.actors

import dev.aleksrychkov.scrooge.component.transaction.form.internal.udf.FormCommand
import dev.aleksrychkov.scrooge.component.transaction.form.internal.udf.FormEvent
import dev.aleksrychkov.scrooge.feature.transaction.GetTransactionResult
import dev.aleksrychkov.scrooge.feature.transaction.GetTransactionUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class LoadTransactionDelegate(
    private val useCase: Lazy<GetTransactionUseCase>,
) {
    suspend operator fun invoke(cmd: FormCommand.LoadTransaction): Flow<FormEvent> {
        return when (val result = useCase.value.invoke(cmd.transactionId)) {
            is GetTransactionResult.Success -> {
                flowOf(FormEvent.Internal.SuccessLoadTransaction(entity = result.transaction))
            }

            else -> {
                flowOf(FormEvent.Internal.FailedLoadTransaction)
            }
        }
    }
}
