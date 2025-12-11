package dev.aleksrychkov.scrooge.component.transaction.form

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.component.transaction.form.internal.TransactionFormComponentInternal
import dev.aleksrychkov.scrooge.component.transaction.form.internal.composables.FormAmount
import dev.aleksrychkov.scrooge.component.transaction.form.internal.composables.FormCategory
import dev.aleksrychkov.scrooge.component.transaction.form.internal.composables.FormDate
import dev.aleksrychkov.scrooge.component.transaction.form.internal.composables.FormTags
import dev.aleksrychkov.scrooge.component.transaction.form.internal.composables.FormTopAppBar
import dev.aleksrychkov.scrooge.component.transaction.form.internal.modal.CategoryModal
import dev.aleksrychkov.scrooge.component.transaction.form.internal.modal.CurrencyModal
import dev.aleksrychkov.scrooge.component.transaction.form.internal.modal.TagModal
import dev.aleksrychkov.scrooge.component.transaction.form.internal.udf.FormEffect
import dev.aleksrychkov.scrooge.component.transaction.form.internal.udf.FormState
import dev.aleksrychkov.scrooge.core.designsystem.composables.DialogSnackbarHost
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.HalfNormal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import dev.aleksrychkov.scrooge.core.resources.R as Resources

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
    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }
    DisposableEffect(component) {
        val job = scope.launch {
            component.effects
                .onEach { effect ->
                    when (effect) {
                        is FormEffect.ShowErrorMessage -> {
                            snackbarHostState.showSnackbar(message = effect.message)
                        }
                    }
                }
                .collect()
        }
        onDispose {
            job.cancel()
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        snackbarHost = {
            DialogSnackbarHost(
                snackbarHostState = snackbarHostState,
            )
        },
        topBar = {
            FormTopAppBar(
                component = component,
            )
        }
    ) { innerPadding ->
        FormContent(
            modifier = Modifier.padding(innerPadding),
            component = component,
        )
    }
    CategoryModal(
        component = component,
    )
    TagModal(
        component = component,
    )
    CurrencyModal(
        component = component,
    )
}

@Composable
private fun FormContent(
    modifier: Modifier,
    component: TransactionFormComponentInternal,
) {
    val state by component.state.collectAsStateWithLifecycle()

    FormContent(
        modifier = modifier,
        state = state,
        amountChanged = component::setAmount,
        openCategoryModal = component::openCategoryModal,
        openTagModal = component::openTagModal,
        openCurrencyModal = component::openCurrencyModal,
        onDateSelected = component::onDateSelected,
        removeTag = component::removeTag,
        submitClicked = component::onSubmitClicked,
        deleteClicked = component::onDeleteClicked,
    )
}

@Composable
@Suppress("LongParameterList")
private fun FormContent(
    modifier: Modifier,
    state: FormState,
    amountChanged: (String) -> Unit,
    openCategoryModal: () -> Unit,
    openTagModal: () -> Unit,
    openCurrencyModal: () -> Unit,
    onDateSelected: (Long?) -> Unit,
    removeTag: (TagEntity) -> Unit,
    submitClicked: () -> Unit,
    deleteClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .verticalScroll(rememberScrollState())
            .padding(Large)
    ) {
        FormAmount(
            modifier = Modifier.fillMaxWidth(),
            amount = state.amount,
            isEditing = state.transactionId != null,
            isLoading = state.isLoading,
            currency = state.currency.currencySymbol,
            amountChanged = amountChanged,
            openCurrencyModal = openCurrencyModal,
        )

        Spacer(modifier = Modifier.height(Normal))

        FormCategory(
            modifier = Modifier.fillMaxWidth(),
            category = state.category,
            openCategoryModal = openCategoryModal,
        )

        Spacer(modifier = Modifier.height(Normal))

        FormDate(
            modifier = Modifier.fillMaxWidth(),
            timestamp = state.timestamp,
            date = state.timestampReadable,
            onDateSelected = onDateSelected,
        )

        Spacer(modifier = Modifier.height(HalfNormal))

        FormTags(
            modifier = Modifier.fillMaxWidth(),
            tags = state.tags,
            openTagModal = openTagModal,
            removeTag = removeTag,
        )

        ExtendedFloatingActionButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Normal),
            onClick = submitClicked,
        ) {
            Text(stringResource(Resources.string.submit))
        }

        DeleteTransaction(
            state = state,
            deleteClicked = deleteClicked,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeleteTransaction(
    state: FormState,
    deleteClicked: () -> Unit,
) {
    if (state.transactionId == null) return
    val isConfirmationAlertVisible = remember { mutableStateOf(false) }
    TextButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Normal),
        shape = MaterialTheme.shapes.large,
        onClick = {
            isConfirmationAlertVisible.value = true
        }
    ) {
        Text(stringResource(Resources.string.delete))
    }
    if (!isConfirmationAlertVisible.value) return
    AlertDialog(
        onDismissRequest = {
            isConfirmationAlertVisible.value = false
        },
        text = {
            Text(
                text = stringResource(Resources.string.form_delete_confirmation_text)
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    isConfirmationAlertVisible.value = false
                    deleteClicked()
                }
            ) {
                Text(text = stringResource(Resources.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    isConfirmationAlertVisible.value = false
                }
            ) {
                Text(text = stringResource(Resources.string.dismiss))
            }
        },
    )
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun FormContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            FormContent(
                modifier = Modifier,
                state = FormState(),
                amountChanged = {},
                openCategoryModal = {},
                openTagModal = {},
                openCurrencyModal = {},
                onDateSelected = {},
                removeTag = {},
                submitClicked = {},
                deleteClicked = {},
            )
        }
    }
}
