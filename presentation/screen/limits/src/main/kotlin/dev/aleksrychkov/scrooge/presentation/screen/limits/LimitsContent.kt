package dev.aleksrychkov.scrooge.presentation.screen.limits

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsInputTextFieldsColors
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsSecondaryCard
import dev.aleksrychkov.scrooge.core.designsystem.composables.animateElevation
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppBarShadow
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal2X
import dev.aleksrychkov.scrooge.core.designsystem.theme.Small
import dev.aleksrychkov.scrooge.core.designsystem.utils.AmountInputTransformation
import dev.aleksrychkov.scrooge.core.designsystem.utils.AmountOutputTransformation
import dev.aleksrychkov.scrooge.core.entity.AMOUNT_DELIMITER
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.LimitsComponentInternal
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.modal.LimitPeriodModal
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitDto
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.max
import dev.aleksrychkov.scrooge.core.resources.R as Resources

private const val MINIMAL_SCROLL_VALUE_TO_CAST_SHADOW = 10

@Composable
fun LimitsContent(
    modifier: Modifier,
    component: LimitsComponent,
) {
    LimitsContent(
        modifier = modifier,
        component = component as LimitsComponentInternal,
    )
}

@Composable
private fun LimitsContent(
    modifier: Modifier,
    component: LimitsComponentInternal,
) {
    val state by component.state.collectAsStateWithLifecycle()

    LimitsContent(
        modifier = modifier,
        state = state,
        onBackPressed = component::onBackPressed,
        onAddLimitClicked = component::onAddLimitClicked,
        onAmountChanged = component::onAmountChanged,
        onDeleteLimitClicked = component::onDeleteLimitClicked,
        onPeriodSelected = component::onPeriodChanged,
    )
}

@Composable
private fun LimitsContent(
    modifier: Modifier,
    state: LimitsState,
    onBackPressed: () -> Unit,
    onAddLimitClicked: () -> Unit,
    onAmountChanged: (Long, String) -> Unit,
    onDeleteLimitClicked: (Long) -> Unit,
    onPeriodSelected: (Long, String) -> Unit,
) {
    val scrollState = rememberLazyListState()
    Scaffold(
        modifier = modifier,
        topBar = {
            LimitsAppBar(
                scrollState = scrollState,
                onBackPressed = onBackPressed,
            )
        }
    ) { innerPadding ->
        ContentIme(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = state,
            scrollState = scrollState,
            onAddLimitClicked = onAddLimitClicked,
            onAmountChanged = onAmountChanged,
            onDeleteLimitClicked = onDeleteLimitClicked,
            onPeriodSelected = onPeriodSelected,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LimitsAppBar(
    scrollState: LazyListState,
    onBackPressed: () -> Unit,
) {
    val headerElevation by remember {
        derivedStateOf {
            if (scrollState.firstVisibleItemScrollOffset > MINIMAL_SCROLL_VALUE_TO_CAST_SHADOW) {
                AppBarShadow
            } else {
                0.dp
            }
        }
    }
    val animatedElevation by headerElevation.animateElevation()

    Surface(
        Modifier.fillMaxWidth(),
        shadowElevation = animatedElevation,
    ) {
        TopAppBar(
            title = {
                Text(text = stringResource(Resources.string.limits))
            },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Resources.string.back),
                    )
                }
            }
        )
    }
}

@Composable
private fun ContentIme(
    modifier: Modifier,
    scrollState: LazyListState,
    state: LimitsState,
    onAddLimitClicked: () -> Unit,
    onAmountChanged: (Long, String) -> Unit,
    onDeleteLimitClicked: (Long) -> Unit,
    onPeriodSelected: (Long, String) -> Unit,
) {
    Scaffold(
        modifier = modifier,
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .consumeWindowInsets(contentPadding)
                .imePadding()
        ) {
            Content(
                modifier = Modifier.fillMaxSize(),
                state = state,
                scrollState = scrollState,
                onAddLimitClicked = onAddLimitClicked,
                onAmountChanged = onAmountChanged,
                onDeleteLimitClicked = onDeleteLimitClicked,
                onPeriodSelected = onPeriodSelected,
            )
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    state: LimitsState,
    scrollState: LazyListState,
    onAddLimitClicked: () -> Unit,
    onAmountChanged: (Long, String) -> Unit,
    onDeleteLimitClicked: (Long) -> Unit,
    onPeriodSelected: (Long, String) -> Unit,
) {
    AnimatedVisibility(
        visible = state.editable.isEmpty() && !state.isLoading,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        NoSavedLimits(
            modifier = modifier,
            onAddLimitClicked = onAddLimitClicked,
        )
    }

    AnimatedVisibility(
        visible = state.editable.isNotEmpty(),
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Limits(
            modifier = modifier,
            scrollState = scrollState,
            limits = state.editable,
            onAddLimitClicked = onAddLimitClicked,
            onAmountChanged = onAmountChanged,
            onDeleteLimitClicked = onDeleteLimitClicked,
            onPeriodSelected = onPeriodSelected,
        )
    }
}

@Composable
private fun NoSavedLimits(
    modifier: Modifier,
    onAddLimitClicked: () -> Unit,
) {
    Box(
        modifier = modifier
            .padding(Large),
        contentAlignment = Alignment.Center,
    ) {
        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Normal),
            onClick = onAddLimitClicked,
        ) {
            Text(stringResource(Resources.string.limits_add))
        }
    }
}

@Composable
private fun Limits(
    modifier: Modifier,
    scrollState: LazyListState,
    limits: ImmutableList<LimitDto>,
    onAddLimitClicked: () -> Unit,
    onAmountChanged: (Long, String) -> Unit,
    onDeleteLimitClicked: (Long) -> Unit,
    onPeriodSelected: (Long, String) -> Unit,
) {
    Box(
        modifier = modifier
    ) {
        LimitsList(
            modifier = Modifier.fillMaxWidth(),
            limits = limits,
            scrollState = scrollState,
            onAmountChanged = onAmountChanged,
            onDeleteLimitClicked = onDeleteLimitClicked,
            onAddLimitClicked = onAddLimitClicked,
            onPeriodSelected = onPeriodSelected,
        )
    }
}

@Composable
private fun LimitsList(
    modifier: Modifier,
    scrollState: LazyListState,
    limits: ImmutableList<LimitDto>,
    onAmountChanged: (Long, String) -> Unit,
    onDeleteLimitClicked: (Long) -> Unit,
    onAddLimitClicked: () -> Unit,
    onPeriodSelected: (Long, String) -> Unit,
) {
    LazyColumn(
        modifier = modifier.animateContentSize(),
        state = scrollState,
        contentPadding = PaddingValues(bottom = Large, top = Large),
    ) {
        items(
            items = limits,
            key = { item -> item.id.toString() }
        ) { item ->
            LimitsItem(
                modifier = Modifier
                    .animateItem()
                    .fillMaxWidth()
                    .padding(bottom = Normal),
                item = item,
                onAmountChanged = onAmountChanged,
                onDeleteLimitClicked = onDeleteLimitClicked,
                onPeriodSelected = onPeriodSelected,
            )
        }

        item {
            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Large),
                onClick = onAddLimitClicked,
            ) {
                Text(stringResource(Resources.string.limits_add))
            }
        }
    }
}

@Composable
private fun LimitsItem(
    modifier: Modifier,
    item: LimitDto,
    onAmountChanged: (Long, String) -> Unit,
    onDeleteLimitClicked: (Long) -> Unit,
    onPeriodSelected: (Long, String) -> Unit,
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .padding(horizontal = Large),
    ) {
        LimitsItemPeriod(
            item = item,
            onPeriodSelected = onPeriodSelected,
        )

        Spacer(modifier = Modifier.width(Normal))

        LimitsItemAmount(
            item = item,
            onAmountChanged = onAmountChanged,
        )

        Spacer(modifier = Modifier.width(Normal))

        LimitsItemOptions(
            item = item,
            onDeleteLimitClicked = onDeleteLimitClicked,
        )
    }
}

@Composable
private fun LimitsItemPeriod(
    item: LimitDto,
    onPeriodSelected: (Long, String) -> Unit,
) {
    val periodDaily = stringResource(Resources.string.limits_daily)
    val periodWeekly = stringResource(Resources.string.limits_weekly)
    val periodMonthly = stringResource(Resources.string.limits_monthly)
    val textMeasurer = rememberTextMeasurer()
    val textStyle = MaterialTheme.typography.titleMedium
    val density = LocalDensity.current
    val width = remember(periodDaily, density) {
        val resultDaily = textMeasurer.measure(
            text = periodDaily,
            style = textStyle,
        ).size.width
        val resultWeekly = textMeasurer.measure(
            text = periodWeekly,
            style = textStyle,
        ).size.width
        val resultMonthly = textMeasurer.measure(
            text = periodMonthly,
            style = textStyle,
        ).size.width
        with(density) {
            max(max(resultDaily, resultWeekly), resultMonthly).toDp() + Normal2X
        }
    }

    var showModal by remember { mutableStateOf(false) }
    DsSecondaryCard(
        modifier = Modifier
            .width(width)
            .fillMaxHeight()
            .clip(CardDefaults.shape)
            .debounceClickable {
                showModal = true
            },
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = Normal),
                text = item.periodText,
                color = MaterialTheme.colorScheme.primary,
                style = textStyle,
                textAlign = TextAlign.Center,
            )
        }
    }
    if (showModal) {
        LimitPeriodModal(
            current = item.periodText,
            onPeriodSelected = {
                onPeriodSelected(item.id, it)
            },
            onDismiss = { showModal = false }
        )
    }
}

@Composable
private fun RowScope.LimitsItemAmount(
    item: LimitDto,
    onAmountChanged: (Long, String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    DsSecondaryCard(
        modifier = Modifier
            .weight(weight = 1f),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Small),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val amountTextFieldState = rememberTextFieldState("")
            LaunchedEffect(key1 = item.amount) {
                if (item.amount != amountTextFieldState.text.toString()) {
                    amountTextFieldState.setTextAndPlaceCursorAtEnd(item.amount)
                }
            }
            LaunchedEffect(amountTextFieldState) {
                snapshotFlow { amountTextFieldState.text.toString() }
                    .collectLatest {
                        onAmountChanged(item.id, it)
                    }
            }
            val outputTransformation = remember(key1 = item.currencySymbol) {
                AmountOutputTransformation(item.currencySymbol)
            }
            val inputTransformation = remember {
                AmountInputTransformation()
            }
            TextField(
                modifier = Modifier
                    .weight(weight = 1f, fill = true)
                    .focusRequester(focusRequester),
                state = amountTextFieldState,
                lineLimits = TextFieldLineLimits.SingleLine,
                label = {
                    Text(text = stringResource(Resources.string.amount))
                },
                placeholder = {
                    val placeholder = "${item.currencySymbol} 0${AMOUNT_DELIMITER}00"
                    Text(
                        text = placeholder,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number,
                ),
                onKeyboardAction = {
                    focusManager.clearFocus()
                },
                colors = DsInputTextFieldsColors(),
                inputTransformation = inputTransformation,
                outputTransformation = outputTransformation,
            )
            TextButton(
                modifier = Modifier.fillMaxHeight(),
                onClick = {},
            ) {
                Text(
                    text = item.currencySymbol,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Composable
private fun LimitsItemOptions(
    item: LimitDto,
    onDeleteLimitClicked: (Long) -> Unit,
) {
    var isConfirmationAlertVisible by remember { mutableStateOf(false) }
    DsSecondaryCard(
        modifier = Modifier
            .wrapContentWidth()
            .fillMaxHeight()
            .clip(CardDefaults.shape)
            .debounceClickable {
                isConfirmationAlertVisible = true
            },
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = Large),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = stringResource(Resources.string.delete),
            )
        }
    }

    if (!isConfirmationAlertVisible) return
    AlertDialog(
        onDismissRequest = {
            isConfirmationAlertVisible = false
        },
        text = {
            Text(text = stringResource(Resources.string.limits_delete_confirmation_text))
        },
        confirmButton = {
            TextButton(
                onClick = {
                    isConfirmationAlertVisible = false
                    onDeleteLimitClicked(item.id)
                }
            ) {
                Text(text = stringResource(Resources.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    isConfirmationAlertVisible = false
                }
            ) {
                Text(text = stringResource(Resources.string.dismiss))
            }
        },
    )
}

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun ContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Content(
                modifier = Modifier.fillMaxSize(),
                state = LimitsState(
                    editable = persistentListOf(
                        LimitDto(
                            id = 0L,
                            periodText = "Monthly",
                            currencyCode = "RUB",
                            currencySymbol = "R",
                            amount = "0.00",
                        )
                    )
                ),
                scrollState = rememberLazyListState(),
                onAddLimitClicked = {},
                onAmountChanged = { _, _ -> },
                onDeleteLimitClicked = { _ -> },
                onPeriodSelected = { _, _ -> },
            )
        }
    }
}
