package dev.aleksrychkov.scrooge.component.transactionform.internal.udf.actors

import dev.aleksrychkov.scrooge.component.transactionform.internal.udf.FormCommand
import dev.aleksrychkov.scrooge.component.transactionform.internal.udf.FormEvent
import dev.aleksrychkov.scrooge.component.transactionform.internal.udf.FormState
import dev.aleksrychkov.scrooge.feature.transaction.CreateTransactionResult
import dev.aleksrychkov.scrooge.feature.transaction.CreateTransactionUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class SubmitDelegate(
    private val createUseCase: Lazy<CreateTransactionUseCase>,
) {

    private companion object {
        const val CENTS = 100L
    }

    suspend operator fun invoke(cmd: FormCommand.Submit): Flow<FormEvent> {
        val state = cmd.state
        val validationEvent = validate(state)
        if (validationEvent != null) {
            return flowOf(validationEvent)
        }
        requireNotNull(state.category)

        val amount: Long = (state.amount.toDouble() * CENTS).toLong()

        val args = CreateTransactionUseCase.Args(
            amount = amount,
            transactionType = state.transactionType,
            category = state.category,
            tags = state.tags.toSet(),
            currency = state.currency,
            timestamp = state.timestamp.toEpochMilliseconds()
        )
        val result = createUseCase.value.invoke(args = args)
        val resultEvent = when (result) {
            CreateTransactionResult.Failure -> FormEvent.Internal.CreateTransactionFailure
            CreateTransactionResult.Success -> FormEvent.Internal.CreateTransactionSuccess
        }
        return flowOf(resultEvent)
    }

    private fun validate(state: FormState): FormEvent? {
        return when {
            state.amount.isBlank() -> FormEvent.Internal.EmptyAmount
            state.category == null -> FormEvent.Internal.EmptyCategory
            else -> null
        }
    }
}
