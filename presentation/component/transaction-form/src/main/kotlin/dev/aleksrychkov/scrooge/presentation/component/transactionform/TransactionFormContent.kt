package dev.aleksrychkov.scrooge.presentation.component.transactionform

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.TransactionFormComponentInternal

@Composable
fun TransactionFormContent(
    modifier: Modifier,
    component: TransactionFormComponent,
    @Suppress("unused") onDone: () -> Unit,
) {
    Content(
        modifier = modifier,
        component = component as TransactionFormComponentInternal,
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    @Suppress("unused") component: TransactionFormComponentInternal,
) {
    Column(modifier.padding(Normal)) {
        Text("Form!")
    }
}
