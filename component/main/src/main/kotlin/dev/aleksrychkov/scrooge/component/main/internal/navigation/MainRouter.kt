package dev.aleksrychkov.scrooge.component.main.internal.navigation

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import dev.aleksrychkov.scrooge.component.main.internal.navigation.MainNavigationConfig.TransactionForm
import dev.aleksrychkov.scrooge.component.main.internal.navigation.MainNavigationConfig.TransactionsList
import dev.aleksrychkov.scrooge.core.router.Destination
import dev.aleksrychkov.scrooge.core.router.DestinationTransactionForm
import dev.aleksrychkov.scrooge.core.router.DestinationTransactionsList
import dev.aleksrychkov.scrooge.core.router.Router

internal class MainRouter(
    val navigation: StackNavigation<MainNavigationConfig>
) : Router {
    override fun open(destination: Destination) {
        when (destination) {
            is DestinationTransactionForm -> {
                navigation.pushNew(
                    TransactionForm(
                        destination = destination,
                    )
                )
            }

            is DestinationTransactionsList -> {
                navigation.pushNew(
                    TransactionsList(
                        destination = destination,
                    )
                )
            }
        }
    }

    override fun close() {
        navigation.pop()
    }
}
