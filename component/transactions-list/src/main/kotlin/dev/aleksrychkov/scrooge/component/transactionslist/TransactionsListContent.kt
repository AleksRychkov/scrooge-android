package dev.aleksrychkov.scrooge.component.transactionslist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.component.transactionslist.internal.TransactionsListComponentInternal
import dev.aleksrychkov.scrooge.component.transactionslist.internal.udf.TransactionsListState
import dev.aleksrychkov.scrooge.core.designsystem.theme.ExpenseColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.HalfNormal
import dev.aleksrychkov.scrooge.core.designsystem.theme.IncomeColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal2X
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.entity.amountToValue
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        shape = CardDefaults.shape,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                    )
                    .clip(CardDefaults.shape),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Normal),
                    fontStyle = FontStyle.Italic,
                    text = group.date
                )

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = HalfNormal, horizontal = Normal)
                )

                group.transactions.forEachIndexed { index, t ->
                    TransactionItem(
                        modifier = Modifier.fillMaxWidth(),
                        index = index,
                        transaction = t,
                        onTransactionClicked = onTransactionClicked,
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionItem(
    modifier: Modifier,
    index: Int,
    transaction: TransactionEntity,
    onTransactionClicked: (TransactionEntity) -> Unit,
) {
    val bgColor = if (index % 2 == 0) {
        Unspecified
    } else {
        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f)
    }
    Column(
        modifier = modifier
            .clickable {
                onTransactionClicked(transaction)
            }
            .background(color = bgColor)
            .padding(Normal),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val (color, icon) = when (transaction.type) {
                TransactionType.Income -> IncomeColor to Resources.drawable.ic_trending_up_24px
                TransactionType.Expense -> ExpenseColor to Resources.drawable.ic_trending_down_24px
            }
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = null,
                tint = color,
            )
            Spacer(Modifier.size(Large))
            Text(
                modifier = Modifier.fillMaxWidth(),
                color = color,
                textAlign = TextAlign.End,
                text = "${transaction.amount.amountToValue()} ${transaction.currency.currencySymbol}",
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Normal),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.padding(end = HalfNormal),
                text = stringResource(Resources.string.category) + ":",
                fontStyle = FontStyle.Italic,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = transaction.category,
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (transaction.tags.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Normal)
            ) {
                Text(
                    modifier = Modifier.padding(end = HalfNormal),
                    text = stringResource(Resources.string.tags) + ":",
                    fontStyle = FontStyle.Italic,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                )

                Text(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    text = transaction.tags.joinToString(),
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                )
            }
        }
    }
}
