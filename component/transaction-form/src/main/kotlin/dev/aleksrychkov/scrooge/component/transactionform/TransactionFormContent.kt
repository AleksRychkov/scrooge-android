package dev.aleksrychkov.scrooge.component.transactionform

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.component.transactionform.internal.TransactionFormComponentInternal
import dev.aleksrychkov.scrooge.component.transactionform.internal.modal.CategoryModal
import dev.aleksrychkov.scrooge.component.transactionform.internal.modal.CurrencyModal
import dev.aleksrychkov.scrooge.component.transactionform.internal.modal.DatePickerModal
import dev.aleksrychkov.scrooge.component.transactionform.internal.modal.TagModal
import dev.aleksrychkov.scrooge.component.transactionform.internal.udf.FormEffect
import dev.aleksrychkov.scrooge.component.transactionform.internal.udf.FormState
import dev.aleksrychkov.scrooge.component.transactionform.internal.utils.rememberAmountVisualTransformation
import dev.aleksrychkov.scrooge.core.designsystem.composables.AppCard
import dev.aleksrychkov.scrooge.core.designsystem.composables.DialogSnackbarHost
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.ColorCurrency
import dev.aleksrychkov.scrooge.core.designsystem.theme.HalfNormal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal2X
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
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

@OptIn(ExperimentalMaterial3Api::class)
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
            TopAppBar(
                title = {
                    Text(text = stringResource(Resources.string.form_add_transaction))
                },
                navigationIcon = {
                    IconButton(onClick = component::onBackClicked) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Resources.string.back),
                        )
                    }
                },
                actions = {
                    TextButton(onClick = component::onSubmitClicked) {
                        Text(text = stringResource(Resources.string.form_submit))
                    }
                }
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

@OptIn(ExperimentalTime::class)
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
) {
    AppCard(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .verticalScroll(rememberScrollState())
            .padding(Large)
    ) {
        Amount(
            modifier = Modifier.fillMaxWidth(),
            amount = state.amount,
            currency = state.currency.currencySymbol,
            amountChanged = amountChanged,
            openCurrencyModal = openCurrencyModal,
        )

        FormDivider()

        Category(
            modifier = Modifier.fillMaxWidth(),
            category = state.category,
            openCategoryModal = openCategoryModal,
        )

        FormDivider()

        Date(
            modifier = Modifier.fillMaxWidth(),
            timestamp = state.timestamp,
            date = state.timestampReadable,
            onDateSelected = onDateSelected,
        )

        FormDivider()

        Tags(
            modifier = Modifier.fillMaxWidth(),
            tags = state.tags,
            openTagModal = openTagModal,
            removeTag = removeTag,
        )

        FormDivider()

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Normal),
            onClick = submitClicked,
        ) {
            Text(stringResource(Resources.string.form_submit))
        }
    }
}

@Composable
private fun Amount(
    modifier: Modifier,
    amount: String,
    currency: String,
    amountChanged: (String) -> Unit,
    openCurrencyModal: () -> Unit,
) {
    Row(
        modifier = modifier
            .height(intrinsicSize = IntrinsicSize.Max)
            .padding(Normal),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val focusManager = LocalFocusManager.current
        val amountFormatter = rememberAmountVisualTransformation(currency = currency)
        TextField(
            modifier = Modifier.weight(weight = 1f, fill = true),
            value = amount,
            singleLine = true,
            label = {
                Text(text = stringResource(Resources.string.form_amount))
            },
            placeholder = {
                val placeholder = amount.ifBlank { "$currency 0.00" }
                Text(text = placeholder)
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number,
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            onValueChange = { value ->
                amountChanged(value)
            },
            colors = TextFieldDefaults.colors().copy(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                unfocusedTextColor = Color.Unspecified,
            ),
            visualTransformation = amountFormatter,
        )
        Spacer(
            modifier = Modifier.width(Normal)
        )

        Button(
            onClick = openCurrencyModal,
            colors = ButtonDefaults.buttonColors().copy(containerColor = ColorCurrency)
        ) {
            Text(text = currency)
        }
    }
}

@Composable
private fun Category(
    modifier: Modifier,
    category: CategoryEntity?,
    openCategoryModal: () -> Unit,
) {
    TextField(
        modifier = modifier
            .height(intrinsicSize = IntrinsicSize.Max)
            .padding(Normal)
            .pointerInput(category) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        openCategoryModal()
                    }
                }
            },
        value = category?.name.orEmpty(),
        singleLine = true,
        label = {
            Text(stringResource(Resources.string.form_category))
        },
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            unfocusedTextColor = Color.Unspecified,
        ),
        readOnly = true,
        onValueChange = { },
    )
}

@Composable
private fun Date(
    modifier: Modifier,
    timestamp: Instant,
    date: String,
    onDateSelected: (Long?) -> Unit,
) {
    var showModal by remember { mutableStateOf(false) }

    TextField(
        modifier = modifier
            .height(intrinsicSize = IntrinsicSize.Max)
            .padding(Normal)
            .pointerInput(date) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            },
        value = date,
        singleLine = true,
        label = {
            Text(stringResource(Resources.string.form_date))
        },
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            unfocusedTextColor = Color.Unspecified,
        ),
        readOnly = true,
        onValueChange = { },
    )

    if (showModal) {
        DatePickerModal(
            timestamp = timestamp,
            onDateSelected = onDateSelected,
            onDismiss = { showModal = false }
        )
    }
}

@Composable
private fun Tags(
    modifier: Modifier,
    tags: ImmutableList<TagEntity>,
    openTagModal: () -> Unit,
    removeTag: (TagEntity) -> Unit,
) {
    FlowRow(
        modifier = modifier.padding(horizontal = Normal),
        horizontalArrangement = Arrangement.spacedBy(space = Normal),
    ) {
        InputChip(
            selected = false,
            label = {
                Text(
                    modifier = Modifier.padding(start = Normal),
                    text = stringResource(Resources.string.tag_add),
                )
            },
            shape = RoundedCornerShape(Normal2X),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = stringResource(Resources.string.tag_add),
                )
            },
            onClick = openTagModal,
        )

        tags.forEach { tag ->
            InputChip(
                modifier = Modifier,
                selected = false,
                label = {
                    Text(
                        modifier = Modifier.padding(end = Normal),
                        text = tag.name,
                    )
                },
                shape = RoundedCornerShape(Normal2X),
                onClick = {},
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(Resources.string.tag_delete),
                        modifier = Modifier.debounceClickable {
                            removeTag(tag)
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun FormDivider() {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = HalfNormal),
        color = DividerDefaults.color.copy(alpha = 0.2f),
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
            )
        }
    }
}
