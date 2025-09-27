package dev.aleksrychkov.scrooge.component.transactioncrud

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
@Suppress("UnusedParameter")
fun TransactionCrudContent(
    modifier: Modifier,
    component: TransactionCrudComponent
) {
    Column(
        modifier = modifier
            .displayCutoutPadding()
            .statusBarsPadding()
    ) {
        Text("Crud")
    }
}
