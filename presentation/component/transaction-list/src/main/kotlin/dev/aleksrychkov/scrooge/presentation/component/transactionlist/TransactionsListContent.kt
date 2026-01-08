package dev.aleksrychkov.scrooge.presentation.component.transactionlist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.TransactionsListComponentInternal
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.composables.TransactionItem
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.composables.TransactionsGroupItem
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf.TransactionsItem
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf.TransactionsListState

@Composable
fun TransactionsListContent(
    modifier: Modifier,
    listState: LazyListState? = null,
    paddingTop: Dp = 0.dp,
    paddingBottom: Dp = 0.dp,
    headerItem: @Composable (() -> Unit)? = null,
    component: TransactionsListComponent,
) {
    Content(
        modifier = modifier,
        listState = listState,
        headerItem = headerItem,
        paddingTop = paddingTop,
        paddingBottom = paddingBottom,
        component = component as TransactionsListComponentInternal,
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    listState: LazyListState? = null,
    headerItem: @Composable (() -> Unit)? = null,
    paddingTop: Dp,
    paddingBottom: Dp,
    component: TransactionsListComponentInternal,
) {
    val state by component.state.collectAsStateWithLifecycle()
    Content(
        modifier = modifier,
        listState = listState,
        headerItem = headerItem,
        paddingTop = paddingTop,
        paddingBottom = paddingBottom,
        state = state,
        onTransactionClicked = component::onTransactionClicked,
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    listState: LazyListState? = null,
    headerItem: @Composable (() -> Unit)? = null,
    paddingTop: Dp,
    paddingBottom: Dp,
    state: TransactionsListState,
    onTransactionClicked: (Long, TransactionType) -> Unit,
) {
    val items = state.pagedTransactions.collectAsLazyPagingItems()
    val listState = listState ?: rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(
            top = paddingTop,
            bottom = paddingBottom,
        ),
    ) {
        if (headerItem != null) {
            item {
                headerItem()
            }
        }

        items(count = items.itemCount) { index ->
            val item = items[index] ?: return@items
            when (item) {
                is TransactionsItem.Group -> TransactionsGroupItem(
                    modifier = Modifier.fillMaxWidth().animateItem(),
                    group = item,
                )

                is TransactionsItem.Item -> TransactionItem(
                    modifier = Modifier.fillMaxWidth().animateItem(),
                    transaction = item,
                    onTransactionClicked = onTransactionClicked,
                )
            }
        }
    }
}
