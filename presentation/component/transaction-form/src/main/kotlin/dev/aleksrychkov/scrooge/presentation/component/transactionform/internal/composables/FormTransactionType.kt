package dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.ExpenseColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.IncomeColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Small
import dev.aleksrychkov.scrooge.core.designsystem.theme.Tinny
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
@NonRestartableComposable
internal fun TransactionType(
    modifier: Modifier = Modifier,
    transactionType: TransactionType,
) {
    val color: Color
    val txt: String
    val icon: ImageVector
    if (transactionType == TransactionType.Income) {
        txt = stringResource(Resources.string.income)
        icon = Icons.Default.AddCircleOutline
        color = IncomeColor
    } else {
        txt = stringResource(Resources.string.expense)
        icon = Icons.Default.RemoveCircleOutline
        color = ExpenseColor
    }

    Row(
        modifier = modifier
            .background(color = color.copy(alpha = 0.25f), shape = CircleShape)
            .padding(vertical = Tinny, horizontal = Normal)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
        )
        Text(
            modifier = Modifier.padding(start = Small),
            text = txt,
            color = color,
        )
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun TransactionTypePreview() {
    AppTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TransactionType(
                transactionType = TransactionType.Income
            )
            Spacer(modifier = Modifier.height(Large))
            TransactionType(
                transactionType = TransactionType.Expense
            )
        }
    }
}
