package dev.aleksrychkov.scrooge.presentation.screen.main.root.internal.navigation

import dev.aleksrychkov.scrooge.core.router.DestinationTransactionForm
import kotlinx.serialization.Serializable

@Serializable
internal sealed interface MainNavigationConfig {
    @Serializable
    data object MainTabs : MainNavigationConfig

    @Serializable
    data class TransactionForm(
        val destination: DestinationTransactionForm,
    ) : MainNavigationConfig
}
