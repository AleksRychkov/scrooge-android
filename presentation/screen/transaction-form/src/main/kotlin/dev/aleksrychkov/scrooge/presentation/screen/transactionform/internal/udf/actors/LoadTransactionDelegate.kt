package dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.udf.actors

import dev.aleksrychkov.scrooge.feature.transaction.GetTransactionResult
import dev.aleksrychkov.scrooge.feature.transaction.GetTransactionUseCase
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.udf.FormCommand
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.udf.FormEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

internal class LoadTransactionDelegate(
    private val useCase: Lazy<GetTransactionUseCase>,
) {
    suspend operator fun invoke(cmd: FormCommand.LoadTransaction): Flow<FormEvent> {
        return when (val result = useCase.value.invoke(cmd.transactionId)) {
            is GetTransactionResult.Success -> {
                result.transactionFlow.map { entity ->
                    if (entity == null) {
                        FormEvent.Internal.FailedLoadTransaction
                    } else {
                        FormEvent.Internal.SuccessLoadTransaction(entity = entity)
                    }
                }
            }

            else -> {
                flowOf(FormEvent.Internal.FailedLoadTransaction)
            }
        }
    }
}
