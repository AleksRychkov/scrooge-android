package dev.aleksrychkov.scrooge.component.main.internal.navigation

import dev.aleksrychkov.scrooge.core.router.DestinationTransactionCrud
import kotlinx.serialization.Serializable

@Serializable
internal sealed interface MainNavigationConfig {
    @Serializable
    data object MainTabs : MainNavigationConfig

    @Serializable
    data class TransactionCrud(
        val destination: DestinationTransactionCrud,
    ) : MainNavigationConfig
}
