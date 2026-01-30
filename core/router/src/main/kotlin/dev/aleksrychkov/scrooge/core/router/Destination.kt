package dev.aleksrychkov.scrooge.core.router

import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination

@Serializable
data class DestinationTransactionForm(
    val transactionId: Long?,
    val transactionType: TransactionType,
) : Destination {
    companion object Companion {
        fun addIncome(): Destination =
            DestinationTransactionForm(
                transactionId = null,
                transactionType = TransactionType.Income,
            )

        fun addExpense(): Destination =
            DestinationTransactionForm(
                transactionId = null,
                transactionType = TransactionType.Expense,
            )

        fun edit(
            transactionId: Long,
            type: TransactionType,
        ): Destination =
            DestinationTransactionForm(
                transactionId = transactionId,
                transactionType = type,
            )
    }
}

@Serializable
data class DestinationReportCategoryTotal(
    val filter: FilterEntity,
) : Destination

@Serializable
data class DestinationTransactions(
    val filter: FilterEntity,
) : Destination

@Serializable
object DestinationLimits : Destination
