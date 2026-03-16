package dev.aleksrychkov.scrooge.presentation.component.transactionform

import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Medium
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.entity.Datestamp
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.CategoryCarouselComponent
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.TransactionFormComponentInternal
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables.FormAmount
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables.FormCalculator
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables.FormCategory
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables.FormComment
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables.FormCurrency
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables.FormDate
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables.FormDeleteTransaction
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables.FormTags
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables.NumPad
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.modal.FormCalculatorModal
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
    scrollState: ScrollState? = null,
    onDone: () -> Unit,
) {
    TransactionFormContent(
        modifier = modifier,
        scrollState = scrollState,
        component = component as TransactionFormComponentInternal,
        onCloseClicked = onDone,
    )
}

@Composable
private fun TransactionFormContent(
    modifier: Modifier,
    component: TransactionFormComponentInternal,
    scrollState: ScrollState? = null,
    onCloseClicked: () -> Unit,
) {
    SideEffects(component = component)

    Content(
        modifier = modifier,
        scrollState = scrollState,
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
    scrollState: ScrollState? = null,
    onCloseClicked: () -> Unit,
) {
    val state by component.state.collectAsStateWithLifecycle()

    ContentIme(
        modifier = modifier,
        state = state,
        scrollState = scrollState,
        carouselComponent = component.categoryCarouselComponent,
        onSubmitClicked = component::submit,
        onDeleteClicked = component::delete,
        onPadClicked = component::appendInput,
        onPadRemoveClicked = component::removeLastFromInput,
        onCommentChanged = component::updateComment,
        onDateSelected = component::updateDate,
        onCurrencyClicked = component::openCurrencyModal,
        openTagModal = component::openTagModal,
        onCalculatorClicked = component::openCalculatorModal,
    )

    DoneCheck(
        state = state,
        onClose = onCloseClicked,
    )

    FormTagModal(
        component = component,
        tags = state.tags,
        setTags = component::setTags,
    )
    FromCurrencyModal(
        component = component,
    )
    FormCalculatorModal(
        component = component,
    )
}

@Composable
private fun DoneCheck(
    state: FormState,
    onClose: () -> Unit,
) {
    if (state.isDone) onClose()
}

@Composable
private fun ContentIme(
    modifier: Modifier,
    state: FormState,
    scrollState: ScrollState? = null,
    carouselComponent: CategoryCarouselComponent,
    onSubmitClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onPadClicked: (String) -> Unit,
    onPadRemoveClicked: () -> Unit,
    onCommentChanged: (String) -> Unit,
    onDateSelected: (Long?) -> Unit,
    onCurrencyClicked: () -> Unit,
    openTagModal: () -> Unit,
    onCalculatorClicked: () -> Unit,
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
                scrollState = scrollState,
                carouselComponent = carouselComponent,
                onSubmitClicked = onSubmitClicked,
                onDeleteClicked = onDeleteClicked,
                onPadClicked = onPadClicked,
                onPadRemoveClicked = onPadRemoveClicked,
                onCommentChanged = onCommentChanged,
                onDateSelected = onDateSelected,
                onCurrencyClicked = onCurrencyClicked,
                onTagClicked = openTagModal,
                onCalculatorClicked = onCalculatorClicked,
            )
        }
    }
}

@Composable
private fun FormContent(
    modifier: Modifier,
    state: FormState,
    scrollState: ScrollState? = null,
    carouselComponent: CategoryCarouselComponent,
    onSubmitClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onPadClicked: (String) -> Unit,
    onPadRemoveClicked: () -> Unit,
    onCommentChanged: (String) -> Unit,
    onDateSelected: (Long?) -> Unit,
    onCurrencyClicked: () -> Unit,
    onTagClicked: () -> Unit,
    onCalculatorClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .verticalScroll(scrollState ?: rememberScrollState())
            .padding(horizontal = Large)
            .padding(bottom = Medium),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        FormAmount(
            modifier = Modifier.fillMaxWidth(),
            amount = state.amount,
            currencySymbol = state.currency.currencySymbol,
        )

        Spacer(modifier = Modifier.weight(1f))

        Controls(
            modifier = Modifier.fillMaxWidth(),
            tagsCount = state.tags.size,
            datestamp = state.datestamp,
            datestampReadable = state.datestampReadable,
            currencySymbol = state.currency.currencySymbol,
            onTagClicked = onTagClicked,
            onDateSelected = onDateSelected,
            onCurrencyClicked = onCurrencyClicked,
            onCalculatorClicked = onCalculatorClicked,
        )

        Spacer(modifier = Modifier.height(Normal))

        FormComment(
            modifier = Modifier.fillMaxWidth(),
            isLoading = state.isLoading,
            isEditing = state.transactionId != null,
            comment = state.comment,
            onCommentChanged = onCommentChanged,
        )

        Spacer(modifier = Modifier.height(Normal))

        FormCategory(
            modifier = Modifier.fillMaxWidth(),
            component = carouselComponent,
            category = state.category,
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
                .padding(top = Large),
            onClick = onSubmitClicked,
        ) {
            Text(stringResource(Resources.string.save))
        }

        FormDeleteTransaction(
            modifier = Modifier.fillMaxWidth(),
            transactionId = state.transactionId,
            onDeleteClicked = onDeleteClicked,
        )
    }
}

@Composable
private fun Controls(
    modifier: Modifier = Modifier,
    tagsCount: Int,
    datestamp: Datestamp,
    datestampReadable: String,
    currencySymbol: String,
    onTagClicked: () -> Unit,
    onDateSelected: (Long?) -> Unit,
    onCurrencyClicked: () -> Unit,
    onCalculatorClicked: () -> Unit,
) {
    FlowRow(
        modifier = modifier.height(54.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        FormTags(
            modifier = Modifier.fillMaxHeight(),
            openTagModal = onTagClicked,
            tagsCount = tagsCount,
        )

        FormDate(
            modifier = Modifier.fillMaxHeight(),
            datestamp = datestamp,
            datestampReadable = datestampReadable,
            onDateSelected = onDateSelected,
        )

        FormCurrency(
            modifier = Modifier.fillMaxHeight(),
            currencySymbol = currencySymbol,
            onCurrencyClicked = onCurrencyClicked,
        )

        FormCalculator(
            modifier = Modifier.fillMaxHeight(),
            onCalculatorClicked = onCalculatorClicked,
        )
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun FormContentPreview() {
    AppTheme(
        useDarkTheme = false
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            FormContent(
                modifier = Modifier.fillMaxSize(),
                state = FormState(
                    transactionId = 1,
                    datestampReadable = "Today",
                ),
                carouselComponent = CategoryCarouselComponent.stub(),
                onSubmitClicked = {},
                onDeleteClicked = {},
                onPadClicked = { _ -> },
                onPadRemoveClicked = {},
                onCommentChanged = { _ -> },
                onDateSelected = { _ -> },
                onCurrencyClicked = {},
                onTagClicked = {},
                onCalculatorClicked = {},
            )
        }
    }
}
