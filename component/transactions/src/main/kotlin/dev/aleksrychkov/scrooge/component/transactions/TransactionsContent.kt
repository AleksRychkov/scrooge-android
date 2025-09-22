package dev.aleksrychkov.scrooge.component.transactions

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dev.aleksrychkov.scrooge.component.transactions.internal.TransactionsComponentInternal
import dev.aleksrychkov.scrooge.component.transactions.internal.composables.AddTransactionFab

@Composable
fun TransactionsContent(
    modifier: Modifier,
    component: TransactionsComponent
) {
    TransactionsContent(
        modifier = modifier,
        component = component as TransactionsComponentInternal,
    )
}

@Suppress("unused")
@Composable
private fun TransactionsContent(
    modifier: Modifier,
    component: TransactionsComponentInternal
) {
    Box(
        modifier = modifier
            .displayCutoutPadding()
            .statusBarsPadding()
    ) {
        Column {
            Text("Home")

            val ctx = LocalContext.current
            Button(onClick = {
                Toast.makeText(ctx, "ASD", Toast.LENGTH_SHORT).show()
            }) {
                Text("ASD")
            }
        }

        AddTransactionFab(
            onIncomeClicked = {},
            onExpenseClicked = {},
        )
    }
}
