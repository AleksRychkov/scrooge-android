package dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.udf.actors

import dev.aleksrychkov.scrooge.feature.transaction.CreateTransactionResult
import dev.aleksrychkov.scrooge.feature.transaction.CreateTransactionUseCase
import dev.aleksrychkov.scrooge.feature.transaction.EditTransactionResult
import dev.aleksrychkov.scrooge.feature.transaction.EditTransactionUseCase
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.udf.FormCommand
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.udf.FormEvent
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.udf.FormState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class SubmitDelegate(
    private val createUseCase: Lazy<CreateTransactionUseCase>,
    private val editUseCase: Lazy<EditTransactionUseCase>,
) {
    private companion object {
        val amountRegex = Regex("[^0-9,]")
    }

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
            CreateTransactionResult.Failure -> FormEvent.Internal.SubmitTransactionFailure
            CreateTransactionResult.Success -> FormEvent.Internal.SubmitTransactionSuccess
        }
        return flowOf(resultEvent)
    }

    private suspend fun edit(state: FormState): Flow<FormEvent> {
        requireNotNull(state.category)
        requireNotNull(state.transactionId)

        val amount: Long = state.amount.toCents()

        val args = EditTransactionUseCase.Args(
            transactionId = state.transactionId,
            amount = amount,
            transactionType = state.transactionType,
            category = state.category,
            tags = state.tags.toSet(),
            currency = state.currency,
            timestamp = state.timestamp.toEpochMilliseconds()
        )

        val result = editUseCase.value.invoke(args = args)
        val resultEvent = when (result) {
            EditTransactionResult.Failure -> FormEvent.Internal.SubmitTransactionFailure
            EditTransactionResult.Success -> FormEvent.Internal.SubmitTransactionSuccess
        }
        return flowOf(resultEvent)
    }

    private fun String.toCents(): Long {
        val clean = this.replace(amountRegex, "")
        val parts = clean.split(",")

        val euros = parts.getOrNull(0).orEmpty()
        val cents = parts.getOrNull(1).orEmpty()

        val centsPadded = when (cents.length) {
            0 -> "00"
            1 -> cents + "0"
            else -> cents.take(2)
        }
        return (euros.ifEmpty { "0" } + centsPadded).toLong()
    }
}
