package dev.aleksrychkov.scrooge.component.transactionslist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.component.transactionslist.internal.TransactionsListComponentInternal
import dev.aleksrychkov.scrooge.component.transactionslist.internal.composables.TransactionsGroupItem
import dev.aleksrychkov.scrooge.component.transactionslist.internal.udf.TransactionsListState
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal2X
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import kotlinx.coroutines.delay

@Composable
fun TransactionsListContent(
    modifier: Modifier,
    paddingTop: Int = 0,
    paddingBottom: Int = 0,
    component: TransactionsListComponent,
) {
    Content(
        modifier = modifier,
        paddingTop = paddingTop,
        paddingBottom = paddingBottom,
        component = component as TransactionsListComponentInternal,
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    paddingTop: Int,
    paddingBottom: Int = 0,
    component: TransactionsListComponentInternal,
) {
    val state by component.state.collectAsStateWithLifecycle()
    Content(
        modifier = modifier,
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
    paddingTop: Int,
    paddingBottom: Int = 0,
    state: TransactionsListState,
    onTransactionClicked: (TransactionEntity) -> Unit,
    onListStateChanged: (Pair<Int, Int>) -> Unit,
) {
    val listState = rememberLazyListState()
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
    val density = LocalDensity.current
    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(
            top = with(density) { paddingTop.toDp() },
            bottom = with(density) { paddingBottom.toDp() },
        ),
        verticalArrangement = Arrangement.spacedBy(Normal2X),
    ) {
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
