package dev.aleksrychkov.scrooge.component.main.internal.navigation

import android.os.Looper
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import dev.aleksrychkov.scrooge.component.main.internal.navigation.MainNavigationConfig.TransactionForm
import dev.aleksrychkov.scrooge.core.router.Destination
import dev.aleksrychkov.scrooge.core.router.DestinationTransactionForm
import dev.aleksrychkov.scrooge.core.router.Router

internal class MainRouter(
    val navigation: StackNavigation<MainNavigationConfig>
) : Router {
    override fun open(destination: Destination) {
        validateThread()

        when (destination) {
            is DestinationTransactionForm -> {
                navigation.pushNew(
                    TransactionForm(
                        destination = destination,
                    )
                )
            }
        }
    }

    override fun close() {
        validateThread()

        navigation.pop()
    }

    private fun validateThread() {
        check(Thread.currentThread() === Looper.getMainLooper().thread) {
            "Expected to be called on the main thread, but was ${Thread.currentThread().name}"
        }
    }
}
