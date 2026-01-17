package dev.aleksrychkov.scrooge.presentation.screen.transactions

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.aleksrychkov.scrooge.core.designsystem.composables.animateElevation
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppBarShadow
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.TransactionsListContent
import dev.aleksrychkov.scrooge.presentation.screen.transactions.internal.TransactionsComponentInternal
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun TransactionsContent(
    modifier: Modifier,
    component: TransactionsComponent,
) {
    TransactionsContent(
        modifier = modifier,
        component = component as TransactionsComponentInternal,
    )
}

@Composable
private fun TransactionsContent(
    modifier: Modifier,
    component: TransactionsComponentInternal,
) {
    val contentListState = rememberLazyListState()
    Scaffold(
        modifier = modifier,
        topBar = {
            TransactionAppBar(
                contentListState = contentListState,
                component = component,
            )
        }
    ) { innerPadding ->
        TransactionsListContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            listState = contentListState,
            component = component.transactionsListComponent,
            paddingBottom = Large,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionAppBar(
    contentListState: LazyListState,
    component: TransactionsComponentInternal,
) {
    val headerElevation by remember {
        derivedStateOf {
            if (contentListState.firstVisibleItemScrollOffset > 0 ||
                contentListState.firstVisibleItemIndex != 0
            ) {
                AppBarShadow
            } else {
                0.dp
            }
        }
    }
    val animatedElevation by headerElevation.animateElevation()

    Surface(
        Modifier.fillMaxWidth(),
        shadowElevation = animatedElevation,
    ) {
        TopAppBar(
            title = {
                Text(text = stringResource(Resources.string.transactions))
            },
            navigationIcon = {
                IconButton(onClick = component::onBackPressed) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Resources.string.back),
                    )
                }
            },
        )
    }
}
