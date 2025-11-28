package dev.aleksrychkov.scrooge.component.transactionslist.internal.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import dev.aleksrychkov.scrooge.component.transactionslist.internal.udf.TransactionsGroup
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun TransactionsGroupItem(
    modifier: Modifier,
    group: TransactionsGroup,
    onTransactionClicked: (TransactionEntity) -> Unit,
) {
    Column(
        modifier = modifier
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
            text = group.date,
            style = MaterialTheme.typography.titleMedium,
        )

        group.transactions.forEach { t ->
            TransactionItem(
                modifier = Modifier.fillMaxWidth(),
                transaction = t,
                onTransactionClicked = onTransactionClicked,
            )
        }
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun FormContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            TransactionsGroupItem(
                modifier = Modifier.fillMaxWidth(),
                group = TransactionsGroup(
                    date = "26.11.2025",
                    transactions = persistentListOf(
                        TransactionEntity.DUMMY,
                        TransactionEntity.DUMMY.copy(
                            amount = 345,
                            type = TransactionType.Expense
                        )
                    )
                ),
                onTransactionClicked = {},
            )
        }
    }
}
