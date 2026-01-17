package dev.aleksrychkov.scrooge.presentation.screen.transactions.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.router.Router
import dev.aleksrychkov.scrooge.core.router.context.RouterComponentContext
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.TransactionsListComponent

internal class DefaultTransactionsComponent(
    componentContext: ComponentContext,
    filter: FilterEntity
) : TransactionsComponentInternal, ComponentContext by componentContext {

    private val router: Router by lazy {
        (componentContext as RouterComponentContext).router
    }

    private val _transactionsListComponent: TransactionsListComponent by lazy {
        TransactionsListComponent(
            componentContext = childContext("TransactionsComponentTransactionsListChildContext"),
        ).also {
            it.setFilters(filter)
        }
    }

    override val transactionsListComponent: TransactionsListComponent
        get() = _transactionsListComponent

    override fun onBackPressed() {
        router.close()
    }
}
