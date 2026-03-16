package dev.aleksrychkov.scrooge.presentation.screen.transactionform

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.TransactionFormComponentInternal
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.composables.FormTopAppBar
import dev.aleksrychkov.scrooge.presentation.component.transactionform.TransactionFormContent as FormContent

@Composable
fun TransactionFormContent(
    modifier: Modifier,
    component: TransactionFormComponent
) {
    TransactionFormContent(
        modifier = modifier,
        component = component as TransactionFormComponentInternal,
    )
}

@Composable
private fun TransactionFormContent(
    modifier: Modifier,
    component: TransactionFormComponentInternal,
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        topBar = {
            FormTopAppBar(
                component = component,
                scrollState = scrollState,
                transactionId = component.transactionId,
                transactionType = component.transactionType,
            )
        }
    ) { innerPadding ->
        FormContent(
            modifier = Modifier.padding(innerPadding),
            component = component,
            scrollState = scrollState,
        )
    }
}

@Composable
private fun FormContent(
    modifier: Modifier,
    scrollState: ScrollState,
    component: TransactionFormComponentInternal,
) {
    FormContent(
        modifier = modifier,
        scrollState = scrollState,
        component = component.formComponent,
        onDone = component::onBackClicked,
    )
}
