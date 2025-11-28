package dev.aleksrychkov.scrooge.component.transactionslist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.component.transactionslist.internal.TransactionsListComponentInternal
import dev.aleksrychkov.scrooge.component.transactionslist.internal.composables.TransactionsGroupItem
import dev.aleksrychkov.scrooge.component.transactionslist.internal.udf.TransactionsListState
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal2X
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun TransactionsListContent(
    modifier: Modifier,
    component: TransactionsListComponent,
) {
    TransactionsListContent(
        modifier = modifier,
        component = component as TransactionsListComponentInternal,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionsListContent(
    modifier: Modifier,
    component: TransactionsListComponentInternal,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(Resources.string.details))
                },
                navigationIcon = {
                    IconButton(onClick = component::onBackClicked) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Resources.string.back),
                        )
                    }
                },
                actions = {
                }
            )
        }
    ) { innerPadding ->
        Content(
            modifier = Modifier.padding(innerPadding),
            component = component,
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    component: TransactionsListComponentInternal,
) {
    val state by component.state.collectAsStateWithLifecycle()
    Content(
        modifier = modifier,
        state = state,
        onTransactionClicked = component::onTransactionClicked,
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    state: TransactionsListState,
    onTransactionClicked: (TransactionEntity) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = Large,
            vertical = Normal,
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
