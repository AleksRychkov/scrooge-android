package dev.aleksrychkov.scrooge.component.transactions.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.router.DestinationTransactionForm
import dev.aleksrychkov.scrooge.core.router.Router
import dev.aleksrychkov.scrooge.core.router.context.RouterComponentContext

internal class DefaultTransactionsComponent(
    private val componentContext: ComponentContext
) : TransactionsComponentInternal, ComponentContext by componentContext {

    private val router: Router by lazy {
        (componentContext as RouterComponentContext).router
    }

    override fun addIncome() {
        DestinationTransactionForm.addIncome().let(router::open)
    }

    override fun addExpense() {
        DestinationTransactionForm.addExpense().let(router::open)
    }
}
