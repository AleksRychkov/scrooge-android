package dev.aleksrychkov.scrooge.component.transactionslist

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.aleksrychkov.scrooge.component.transactionslist.internal.TransactionsListComponentInternal
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

// todo remove suppress
@Composable
@Suppress("UnusedParameter")
private fun Content(
    modifier: Modifier,
    component: TransactionsListComponentInternal,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            Text(text = "DETAILS")
        }
    }
}
