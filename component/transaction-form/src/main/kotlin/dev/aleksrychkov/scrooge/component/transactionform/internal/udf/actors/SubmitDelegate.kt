package dev.aleksrychkov.scrooge.component.transactionform.internal.udf.actors

import dev.aleksrychkov.scrooge.component.transactionform.internal.udf.FormCommand
import dev.aleksrychkov.scrooge.component.transactionform.internal.udf.FormEvent
import dev.aleksrychkov.scrooge.component.transactionform.internal.udf.FormState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

internal class SubmitDelegate {
    suspend operator fun invoke(cmd: FormCommand.Submit): Flow<FormEvent> {
        val validationEvent = validate(cmd.state)
        if (validationEvent != null) {
            return flowOf(validationEvent)
        }

        return emptyFlow()
    }

    private fun validate(state: FormState): FormEvent? {
        return when {
            state.amount.isBlank() -> FormEvent.Internal.EmptyAmount
            state.category == null -> FormEvent.Internal.EmptyCategory
            else -> null
        }
    }
}
