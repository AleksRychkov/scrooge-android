package dev.aleksrychkov.scrooge.core.router

import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination

@Serializable
data class DestinationTransactionCrud(
    val transactionId: Long?,
    val transactionType: TransactionType,
) : Destination {
    companion object {
        fun addIncome(): Destination =
            DestinationTransactionCrud(
                transactionId = null,
                transactionType = TransactionType.Income,
            )

        fun addExpense(): Destination =
            DestinationTransactionCrud(
                transactionId = null,
                transactionType = TransactionType.Expense,
            )

        fun edit(
            transactionId: Long,
            type: TransactionType,
        ): Destination =
            DestinationTransactionCrud(
                transactionId = transactionId,
                transactionType = type,
            )
    }
}
