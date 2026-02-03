package dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.udf.actors

import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.entity.toCents
import dev.aleksrychkov.scrooge.feature.transaction.CreateTransactionResult
import dev.aleksrychkov.scrooge.feature.transaction.CreateTransactionUseCase
import dev.aleksrychkov.scrooge.feature.transaction.EditTransactionResult
import dev.aleksrychkov.scrooge.feature.transaction.EditTransactionUseCase
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.udf.FormCommand
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.udf.FormEvent
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.udf.FormState
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class SubmitDelegate(
    private val createUseCase: Lazy<CreateTransactionUseCase>,
    private val editUseCase: Lazy<EditTransactionUseCase>,
) {

    suspend operator fun invoke(cmd: FormCommand.Submit): Flow<FormEvent> {
        val state = cmd.state
        val validationEvent = validate(state)
        if (validationEvent != null) {
            return flowOf(validationEvent)
        }

        return if (cmd.state.transactionId == null) {
            create(state)
        } else {
            edit(state)
        }
    }

    private fun validate(state: FormState): FormEvent? {
        return when {
            state.amount.isBlank() -> FormEvent.Internal.EmptyAmount
            state.category == null -> FormEvent.Internal.EmptyCategory
            else -> null
        }
    }

    private suspend fun create(state: FormState): Flow<FormEvent> {
        requireNotNull(state.category)
        val amount: Long = state.amount.toCents()
        val transaction = TransactionEntity(
            id = -1L,
            amount = amount,
            datestamp = state.datestamp,
            type = state.transactionType,
            category = state.category,
            tags = state.tags.toImmutableSet(),
            currency = state.currency,
            comment = state.comment,
        )
        val result = createUseCase.value.invoke(transaction = transaction)
        val resultEvent = when (result) {
            CreateTransactionResult.Failure -> FormEvent.Internal.SubmitTransactionFailure
            CreateTransactionResult.Success -> FormEvent.Internal.SubmitTransactionSuccess
        }
        return flowOf(resultEvent)
    }

    private suspend fun edit(state: FormState): Flow<FormEvent> {
        requireNotNull(state.category)
        requireNotNull(state.transactionId)

        val amount: Long = state.amount.toCents()

        val transaction = TransactionEntity(
            id = state.transactionId,
            amount = amount,
            datestamp = state.datestamp,
            type = state.transactionType,
            category = state.category,
            tags = state.tags.toImmutableSet(),
            currency = state.currency,
            comment = state.comment,
        )
        val result = editUseCase.value.invoke(transaction = transaction)
        val resultEvent = when (result) {
            EditTransactionResult.Failure -> FormEvent.Internal.SubmitTransactionFailure
            EditTransactionResult.Success -> FormEvent.Internal.SubmitTransactionSuccess
        }
        return flowOf(resultEvent)
    }
}
