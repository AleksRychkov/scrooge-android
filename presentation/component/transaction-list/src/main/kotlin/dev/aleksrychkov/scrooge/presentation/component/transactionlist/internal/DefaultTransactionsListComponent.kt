package dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.router.DestinationTransactionForm
import dev.aleksrychkov.scrooge.core.router.Router
import dev.aleksrychkov.scrooge.core.router.context.RouterComponentContext
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf.TransactionsListActor
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf.TransactionsListEvent
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf.TransactionsListReducer
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf.TransactionsListState
import kotlinx.coroutines.flow.StateFlow

internal class DefaultTransactionsListComponent(
    private val componentContext: ComponentContext,
) : TransactionsListComponentInternal, ComponentContext by componentContext {

    private val router: Router by lazy {
        (componentContext as RouterComponentContext).router
    }

    private val store: Store<TransactionsListState, TransactionsListEvent, Unit> by lazy {
        instanceKeeper.createStore(
            initialState = TransactionsListState(),
            actor = TransactionsListActor.Companion(),
            reducer = TransactionsListReducer(),
        )
    }

    override val state: StateFlow<TransactionsListState>
        get() = store.state

    override fun onBackClicked() {
        router.close()
    }

    override fun onTransactionClicked(transaction: TransactionEntity) {
        DestinationTransactionForm.edit(
            transactionId = transaction.id,
            type = transaction.type,
        ).let(router::open)
    }

    override fun onListStateChanged(position: Pair<Int, Int>) {
        store.handle(
            TransactionsListEvent.External.SetListState(
                index = position.first,
                offset = position.second,
            )
        )
    }

    override fun setFilters(filter: FilterEntity) {
        store.handle(TransactionsListEvent.External.SetFilter(filter = filter))
    }
}
