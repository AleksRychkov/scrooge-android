package dev.aleksrychkov.scrooge.presentation.screen.main.root.internal.navigation

import dev.aleksrychkov.scrooge.core.router.DestinationReportCategoryTotal
import dev.aleksrychkov.scrooge.core.router.DestinationTransactionForm
import dev.aleksrychkov.scrooge.core.router.DestinationTransactions
import kotlinx.serialization.Serializable

@Serializable
internal sealed interface MainNavigationConfig {
    @Serializable
    data object MainTabs : MainNavigationConfig

    @Serializable
    data class TransactionForm(
        val destination: DestinationTransactionForm,
    ) : MainNavigationConfig

    @Serializable
    data class ReportCategoryTotal(
        val destination: DestinationReportCategoryTotal,
    ) : MainNavigationConfig

    @Serializable
    data class Transactions(
        val destination: DestinationTransactions,
    ) : MainNavigationConfig
}
