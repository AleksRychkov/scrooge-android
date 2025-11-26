package dev.aleksrychkov.scrooge.component.transactionslist.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.router.Router
import dev.aleksrychkov.scrooge.core.router.context.RouterComponentContext

// todo remove
@Suppress("UnusedPrivateProperty")
internal class DefaultTransactionsListComponent(
    private val componentContext: ComponentContext,
    private val period: Pair<Long, Long>,
) : TransactionsListComponentInternal, ComponentContext by componentContext {

    private val router: Router by lazy {
        (componentContext as RouterComponentContext).router
    }

    override fun onBackClicked() {
        router.close()
    }
}
