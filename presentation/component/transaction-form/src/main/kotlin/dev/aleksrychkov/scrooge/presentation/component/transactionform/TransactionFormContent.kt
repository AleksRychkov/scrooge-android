package dev.aleksrychkov.scrooge.presentation.component.transactionform

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.TransactionFormComponentInternal
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables.FormAmount
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables.FormClose
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables.FormComment
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables.FormCurrency
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables.FormDate
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables.FormDeleteTransaction
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables.NumPad
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables.TransactionType
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.modal.FormCalculatorModal
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.modal.FormCategoryModal
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.modal.FormTagModal
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.modal.FromCurrencyModal
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.udf.FormEffect
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.udf.FormState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun TransactionFormContent(
    modifier: Modifier,
    component: TransactionFormComponent,
    onDone: () -> Unit,
) {
    TransactionFormContent(
        modifier = modifier,
        component = component as TransactionFormComponentInternal,
        onCloseClicked = onDone,
    )
}

@Composable
private fun TransactionFormContent(
    modifier: Modifier,
    component: TransactionFormComponentInternal,
    onCloseClicked: () -> Unit,
) {
    SideEffects(component = component)

    Content(
        modifier = modifier,
        component = component,
        onCloseClicked = onCloseClicked,
    )
}

@Composable
private fun SideEffects(component: TransactionFormComponentInternal) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    DisposableEffect(component) {
        val job = scope.launch {
            component.effects
                .onEach { effect ->
                    when (effect) {
                        is FormEffect.ShowErrorMessage -> {
                            Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .collect()
        }
        onDispose {
            job.cancel()
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    component: TransactionFormComponentInternal,
    onCloseClicked: () -> Unit,
) {
    val state by component.state.collectAsStateWithLifecycle()

    ContentIme(
        modifier = modifier,
        state = state,
        onSubmitClicked = component::submit,
        onDeleteClicked = component::delete,
        onPadClicked = component::appendInput,
        onPadRemoveClicked = component::removeLastFromInput,
        onCommentChanged = component::updateComment,
        onDateSelected = component::updateDate,
        onCurrencyClicked = component::openCurrencyModal,
        onCloseClicked = onCloseClicked,
    )

    FormCategoryModal(
        component = component,
    )
    FormTagModal(
        component = component,
    )
    FromCurrencyModal(
        component = component,
    )
    FormCalculatorModal(
        component = component,
    )
}

@Composable
private fun ContentIme(
    modifier: Modifier,
    state: FormState,
    onSubmitClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onPadClicked: (String) -> Unit,
    onPadRemoveClicked: () -> Unit,
    onCommentChanged: (String) -> Unit,
    onDateSelected: (Long?) -> Unit,
    onCurrencyClicked: () -> Unit,
    onCloseClicked: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .consumeWindowInsets(contentPadding)
                .imePadding()
        ) {
            FormContent(
                modifier = Modifier.fillMaxSize(),
                state = state,
                onSubmitClicked = onSubmitClicked,
                onDeleteClicked = onDeleteClicked,
                onPadClicked = onPadClicked,
                onPadRemoveClicked = onPadRemoveClicked,
                onCommentChanged = onCommentChanged,
                onDateSelected = onDateSelected,
                onCurrencyClicked = onCurrencyClicked,
                onCloseClicked = onCloseClicked,
            )
        }
    }
}

@Composable
private fun FormContent(
    modifier: Modifier,
    state: FormState,
    onSubmitClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onPadClicked: (String) -> Unit,
    onPadRemoveClicked: () -> Unit,
    onCommentChanged: (String) -> Unit,
    onDateSelected: (Long?) -> Unit,
    onCurrencyClicked: () -> Unit,
    onCloseClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(Large),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FormClose(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f),
                onCloseClicked = onCloseClicked,
            )

            TransactionType(transactionType = state.transactionType)

            FormDeleteTransaction(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f),
                transactionId = state.transactionId,
                onDeleteClicked = onDeleteClicked,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            FormAmount(
                modifier = Modifier,
                amount = state.amount,
                currencySymbol = state.currency.currencySymbol,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.End
        ) {
            FormDate(
                modifier = Modifier.fillMaxHeight(),
                datestamp = state.datestamp,
                datestampReadable = state.datestampReadable,
                onDateSelected = onDateSelected,
            )

            Spacer(modifier = Modifier.width(Normal))

            FormCurrency(
                modifier = Modifier.fillMaxHeight(),
                currencySymbol = state.currency.currencySymbol,
                onCurrencyClicked = onCurrencyClicked,
            )
        }

        Spacer(modifier = Modifier.height(Normal))

        FormComment(
            modifier = Modifier.fillMaxWidth(),
            isLoading = state.isLoading,
            isEditing = state.transactionId != null,
            comment = state.comment,
            onCommentChanged = onCommentChanged,
        )

        Spacer(modifier = Modifier.height(Normal))

        NumPad(
            modifier = Modifier.fillMaxWidth(),
            append = onPadClicked,
            remove = onPadRemoveClicked,
        )

        ExtendedFloatingActionButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Normal, top = Large),
            onClick = onSubmitClicked,
        ) {
            Text(stringResource(Resources.string.save))
        }
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun FormContentPreview() {
    AppTheme(
        useDarkTheme = true
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            FormContent(
                modifier = Modifier.fillMaxSize(),
                state = FormState(
                    transactionId = 1,
                    datestampReadable = "Today"
                ),
                onSubmitClicked = {},
                onDeleteClicked = {},
                onPadClicked = { _ -> },
                onPadRemoveClicked = {},
                onCommentChanged = { _ -> },
                onDateSelected = { _ -> },
                onCurrencyClicked = {},
                onCloseClicked = {},
            )
        }
    }
}
