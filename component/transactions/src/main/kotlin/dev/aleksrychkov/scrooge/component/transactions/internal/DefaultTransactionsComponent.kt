package dev.aleksrychkov.scrooge.component.transactions.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.transactions.internal.udf.TransactionsActor
import dev.aleksrychkov.scrooge.component.transactions.internal.udf.TransactionsEffect
import dev.aleksrychkov.scrooge.component.transactions.internal.udf.TransactionsEvent
import dev.aleksrychkov.scrooge.component.transactions.internal.udf.TransactionsReducer
import dev.aleksrychkov.scrooge.component.transactions.internal.udf.TransactionsState
import dev.aleksrychkov.scrooge.core.router.DestinationTransactionForm
import dev.aleksrychkov.scrooge.core.router.Router
import dev.aleksrychkov.scrooge.core.router.context.RouterComponentContext
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import kotlinx.coroutines.flow.StateFlow

internal class DefaultTransactionsComponent(
    private val componentContext: ComponentContext
) : TransactionsComponentInternal, ComponentContext by componentContext {

    private val router: Router by lazy {
        (componentContext as RouterComponentContext).router
    }

    private val store: Store<TransactionsState, TransactionsEvent, TransactionsEffect> by lazy {
        instanceKeeper.createStore(
            initialState = TransactionsState(),
            actor = TransactionsActor(),
            reducer = TransactionsReducer(),
            startEvent = TransactionsEvent.External.Init,
        )
    }

    override val state: StateFlow<TransactionsState>
        get() = store.state

    override fun addIncome() {
        DestinationTransactionForm.addIncome().let(router::open)
    }

    override fun addExpense() {
        DestinationTransactionForm.addExpense().let(router::open)
    }
}
