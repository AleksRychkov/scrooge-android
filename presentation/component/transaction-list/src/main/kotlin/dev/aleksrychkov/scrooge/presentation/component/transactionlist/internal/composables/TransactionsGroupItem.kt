package dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal2X
import dev.aleksrychkov.scrooge.core.designsystem.theme.Tinny
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf.TransactionsItem
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun TransactionsGroupItem(
    modifier: Modifier,
    group: TransactionsItem.Group,
) {
    Row(
        modifier = modifier
            .padding(horizontal = Large)
            .padding(bottom = Normal, top = Normal2X),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = group.date,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = Tinny),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Top,
        ) {
            group.totals.forEach {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.25f),
                )
            }
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
                group = TransactionsItem.Group(
                    date = "26.11.2025",
                    totals = persistentListOf("-123,00 $"),
                ),
            )
        }
    }
}
