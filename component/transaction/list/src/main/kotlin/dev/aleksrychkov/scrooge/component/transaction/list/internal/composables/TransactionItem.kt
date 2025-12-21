package dev.aleksrychkov.scrooge.component.transaction.list.internal.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.aleksrychkov.scrooge.component.transaction.list.internal.udf.TransactionsItemDto
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.CategoryIconSize
import dev.aleksrychkov.scrooge.core.designsystem.theme.ExpenseColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.IncomeColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Small
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.resources.UncategorizedIcon
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
internal fun TransactionItem(
    modifier: Modifier,
    transaction: TransactionsItemDto,
    onTransactionClicked: (TransactionEntity) -> Unit,
) {
    Row(
        modifier = modifier
            .clickable {
                onTransactionClicked(transaction.ref)
            }
            .padding(horizontal = Large)
            .padding(vertical = Normal),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val transactionColor = when (transaction.type) {
            TransactionType.Income -> IncomeColor
            TransactionType.Expense -> ExpenseColor
        }
        Icon(
            modifier = Modifier
                .height(CategoryIconSize)
                .width(CategoryIconSize)
                .clip(CircleShape)
                .background(Color(transaction.categoryColor))
                .padding(Normal),
            tint = Color.White,
            imageVector = transaction.categoryIcon.icon,
            contentDescription = null,
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

            if (transaction.tags.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Small),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier
                            .height(12.dp)
                            .width(12.dp),
                        imageVector = ImageVector.vectorResource(Resources.drawable.ic_tag_24px),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                    )

                    Text(
                        modifier = Modifier
                            .padding(start = Small)
                            .horizontalScroll(rememberScrollState()),
                        text = transaction.tags,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
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
            TransactionItem(
                modifier = Modifier.fillMaxWidth(),
                transaction = TransactionsItemDto(
                    categoryName = TransactionEntity.DUMMY.category.name,
                    categoryColor = TransactionEntity.DUMMY.category.color,
                    categoryIcon = UncategorizedIcon,
                    amount = "+123,00 $",
                    type = TransactionType.Income,
                    ref = TransactionEntity.DUMMY,
                    tags = TransactionEntity.DUMMY.tags.joinToString(),
                ),
                onTransactionClicked = {},
            )
        }
    }
}
