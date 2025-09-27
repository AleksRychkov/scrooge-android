package dev.aleksrychkov.scrooge.core.router

import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination

@Serializable
data class DestinationTransactionCrud(
    val transactionId: Long?,
    val isIncome: Boolean,
    val isExpense: Boolean,
) : Destination {
    companion object {
        fun addIncome(): Destination =
            DestinationTransactionCrud(
                transactionId = null,
                isIncome = true,
                isExpense = false,
            )

        fun addExpense(): Destination =
            DestinationTransactionCrud(
                transactionId = null,
                isIncome = false,
                isExpense = true,
            )

        fun edit(transactionId: Long): Destination =
            DestinationTransactionCrud(
                transactionId = transactionId,
                isIncome = false,
                isExpense = false,
            )
    }
}
