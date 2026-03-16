package dev.aleksrychkov.scrooge.presentation.component.transactionform

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.DefaultTransactionFormComponent

interface TransactionFormComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
            transactionId: Long?,
            type: TransactionType,
        ): TransactionFormComponent {
            return DefaultTransactionFormComponent(
                componentContext = componentContext,
                transactionId = transactionId,
                transactionType = type,
            )
        }
    }

    fun onSaveClicked()
}
