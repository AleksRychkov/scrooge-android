package dev.aleksrychkov.scrooge.component.transactionform.internal.udf.actors

import dev.aleksrychkov.scrooge.component.transactionform.internal.udf.FormCommand
import dev.aleksrychkov.scrooge.component.transactionform.internal.udf.FormEvent
import dev.aleksrychkov.scrooge.core.router.Router
import dev.aleksrychkov.scrooge.feature.transaction.GetTransactionResult
import dev.aleksrychkov.scrooge.feature.transaction.GetTransactionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

internal class LoadTransactionDelegate(
    private val router: Router,
    private val useCase: Lazy<GetTransactionUseCase>,
) {
    suspend operator fun invoke(cmd: FormCommand.LoadTransaction): Flow<FormEvent> {
        return when (val result = useCase.value.invoke(cmd.transactionId)) {
            is GetTransactionResult.Success -> {
                flowOf(FormEvent.Internal.SuccessLoadTransaction(entity = result.transaction))
            }

            else -> {
                // todo: global snackbar with explanation?
                // todo: retry? but how it will help?
                withContext(Dispatchers.Main) {
                    router.close()
                }
                emptyFlow()
            }
        }
    }
}
