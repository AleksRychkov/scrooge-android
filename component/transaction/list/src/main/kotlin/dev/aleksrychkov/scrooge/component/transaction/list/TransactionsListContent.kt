package dev.aleksrychkov.scrooge.component.transaction.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.component.transaction.list.internal.TransactionsListComponentInternal
import dev.aleksrychkov.scrooge.component.transaction.list.internal.composables.TransactionsGroupItem
import dev.aleksrychkov.scrooge.component.transaction.list.internal.udf.TransactionsListState
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal2X
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import kotlinx.coroutines.delay

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
        onListStateChanged = component::onListStateChanged,
    )
}

@Suppress("MagicNumber")
@Composable
private fun Content(
    modifier: Modifier,
    listState: LazyListState? = null,
    headerItem: @Composable (() -> Unit)? = null,
    paddingTop: Dp,
    paddingBottom: Dp,
    state: TransactionsListState,
    onTransactionClicked: (TransactionEntity) -> Unit,
    onListStateChanged: (Pair<Int, Int>) -> Unit,
) {
    val listState = listState ?: rememberLazyListState()
    LaunchedEffect(state.transactions) {
        // todo: without it not working
        delay(100)
        listState.scrollToItem(
            state.scrollIndex,
            state.scrollOffset
        )
    }
    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }.collect { (index, offset) ->
            if (index == 0 && offset == 0 && state.scrollIndex != 0) return@collect
            onListStateChanged(index to offset)
        }
    }
    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(
            top = paddingTop,
            bottom = paddingBottom,
        ),
        verticalArrangement = Arrangement.spacedBy(Normal2X),
    ) {
        if (headerItem != null) {
            item {
                headerItem()
            }
        }

        items(
            items = state.transactions,
            key = { t -> t.date }
        ) { group ->
            TransactionsGroupItem(
                modifier = Modifier.fillMaxWidth(),
                group = group,
                onTransactionClicked = onTransactionClicked,
            )
        }
    }
}
