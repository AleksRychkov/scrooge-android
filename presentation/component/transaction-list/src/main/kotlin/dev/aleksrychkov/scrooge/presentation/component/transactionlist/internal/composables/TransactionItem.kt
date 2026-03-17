package dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Comment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsCategoryItem
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.ExpenseColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.IncomeColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal2X
import dev.aleksrychkov.scrooge.core.designsystem.theme.Small
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.resources.UncategorizedIcon
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf.TransactionsItem
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
internal fun TransactionItem(
    modifier: Modifier,
    transaction: TransactionsItem.Item,
    onTransactionClicked: (Long, TransactionType) -> Unit,
) {
    Row(
        modifier = modifier
            .debounceClickable {
                onTransactionClicked(transaction.id, transaction.type)
            }
            .padding(horizontal = Large, vertical = Normal),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val transactionColor = when (transaction.type) {
            TransactionType.Income -> IncomeColor
            TransactionType.Expense -> ExpenseColor
        }

        DsCategoryItem(
            color = transaction.categoryColor,
            tint = transaction.categoryTint,
            imageVector = transaction.categoryIcon,
        )

        Column(
            modifier = Modifier.padding(start = Normal),
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                Text(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .padding(end = Normal),
                    text = transaction.categoryName,
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                Text(
                    color = transactionColor,
                    text = transaction.amount,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            TransactionItemMetadata(
                modifier = Modifier.fillMaxWidth(),
                value = transaction.tags,
                imageVector = ImageVector.vectorResource(Resources.drawable.ic_tag_24px),
            )

            TransactionItemMetadata(
                modifier = Modifier.fillMaxWidth(),
                value = transaction.comment,
                imageVector = Icons.AutoMirrored.Rounded.Comment,
            )
        }
    }
}

@Composable
private fun TransactionItemMetadata(
    modifier: Modifier,
    value: String,
    imageVector: ImageVector,
) {
    if (value.isNotEmpty()) {
        Row(
            modifier = modifier.padding(top = Small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            Icon(
                modifier = Modifier
                    .height(12.dp)
                    .width(12.dp),
                imageVector = imageVector,
                contentDescription = null,
                tint = color,
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(IntrinsicSize.Min),
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = Small)
                        .horizontalScroll(rememberScrollState())
                        .padding(end = Normal2X),
                    text = value,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Normal,
                    ),
                    color = color,
                )

                Box(
                    modifier = Modifier
                        .width(Normal2X)
                        .fillMaxHeight()
                        .align(Alignment.CenterEnd)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.background
                                ),
                            )
                        )
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
            val longText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
                " Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                " Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut" +
                " aliquip ex ea commodo consequat."
            TransactionItem(
                modifier = Modifier.fillMaxWidth(),
                transaction = TransactionsItem.Item(
                    id = 0L,
                    categoryName = TransactionEntity.DUMMY.category.name,
                    categoryColor = Color(TransactionEntity.DUMMY.category.color),
                    categoryTint = Color(TransactionEntity.DUMMY.category.color),
                    categoryIcon = UncategorizedIcon.icon,
                    amount = "+123,00 $",
                    type = TransactionType.Income,
                    tags = longText,
                    date = "12.02.2025",
                    comment = longText,
                ),
                onTransactionClicked = { _, _ -> },
            )
        }
    }
}
