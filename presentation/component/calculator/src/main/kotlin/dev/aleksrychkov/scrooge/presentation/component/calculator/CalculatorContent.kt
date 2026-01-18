@file:Suppress("MagicNumber")

package dev.aleksrychkov.scrooge.presentation.component.calculator

import android.annotation.SuppressLint
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Backspace
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsButton
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsSecondaryCard
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Medium
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Tinny
import dev.aleksrychkov.scrooge.core.entity.AMOUNT_DELIMITER
import dev.aleksrychkov.scrooge.core.entity.AMOUNT_DELIMITER_STRING
import dev.aleksrychkov.scrooge.presentation.component.calculator.internal.ADD
import dev.aleksrychkov.scrooge.presentation.component.calculator.internal.ADD_STRING
import dev.aleksrychkov.scrooge.presentation.component.calculator.internal.CLEAN_STRING
import dev.aleksrychkov.scrooge.presentation.component.calculator.internal.CLOSE_PARENTHESES
import dev.aleksrychkov.scrooge.presentation.component.calculator.internal.CLOSE_PARENTHESES_STRING
import dev.aleksrychkov.scrooge.presentation.component.calculator.internal.CalculatorComponentInternal
import dev.aleksrychkov.scrooge.presentation.component.calculator.internal.CalculatorState
import dev.aleksrychkov.scrooge.presentation.component.calculator.internal.DIVIDE
import dev.aleksrychkov.scrooge.presentation.component.calculator.internal.DIVIDE_STRING
import dev.aleksrychkov.scrooge.presentation.component.calculator.internal.MULTIPLY
import dev.aleksrychkov.scrooge.presentation.component.calculator.internal.MULTIPLY_STRING
import dev.aleksrychkov.scrooge.presentation.component.calculator.internal.OPEN_PARENTHESES
import dev.aleksrychkov.scrooge.presentation.component.calculator.internal.OPEN_PARENTHESES_STRING
import dev.aleksrychkov.scrooge.presentation.component.calculator.internal.SUBTRACT
import dev.aleksrychkov.scrooge.presentation.component.calculator.internal.SUBTRACT_STRING
import dev.aleksrychkov.scrooge.presentation.component.calculator.internal.isOperand
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import dev.aleksrychkov.scrooge.core.resources.R as Resources

private const val CURSOR_ANIMATION_DURATION = 600
private const val AUTO_FONT_SIZE_ANIMATION_DURATION = 250
private val MAX_INFIX_TEXT_SIZE = 60.sp
private val MIN_INFIX_TEXT_SIZE = 30.sp

@Composable
fun CalculatorContent(
    modifier: Modifier,
    component: CalculatorComponent,
    callback: (String) -> Unit,
) {
    CalculatorContent(
        modifier = modifier,
        component = component as CalculatorComponentInternal,
        callback = callback,
    )
}

@Composable
private fun CalculatorContent(
    modifier: Modifier,
    component: CalculatorComponentInternal,
    callback: (String) -> Unit,
) {
    val state by component.state.collectAsStateWithLifecycle()
    Content(
        modifier = modifier,
        state = state,
        onInfixInputChanged = component::calculateResult,
        onApplyClicked = { callback(state.result) },
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    state: CalculatorState,
    onInfixInputChanged: (String) -> Unit,
    onApplyClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(horizontal = Large)
            .padding(bottom = Large),
    ) {
        val infix = remember { mutableStateOf("") }

        LaunchedEffect(Unit) {
            snapshotFlow { infix.value }
                .distinctUntilChanged()
                .onEach { onInfixInputChanged(it) }
                .launchIn(this)
        }

        InputBox(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1f, fill = true),
            infix = infix,
            result = state.result,
            resultErrorMessage = state.errorMessage,
        )

        OperatorsBox(
            modifier = Modifier.fillMaxWidth(),
            infix = infix,
        )

        ExtendedFloatingActionButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Normal),
            onClick = onApplyClicked,
        ) {
            Text(stringResource(Resources.string.apply))
        }
    }
}

@Composable
private fun InputBox(
    modifier: Modifier,
    infix: State<String>,
    result: String,
    resultErrorMessage: String?,
) {
    InputBox(
        modifier = modifier,
        infix = infix.value,
        result = result,
        resultErrorMessage = resultErrorMessage,
    )
}

@Composable
private fun InputBox(
    modifier: Modifier,
    infix: String,
    result: String,
    resultErrorMessage: String?,
) {
    DsSecondaryCard(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(Large)
        ) {
            val scrollState = rememberScrollState()
            LaunchedEffect(infix) {
                launch { scrollState.animateScrollTo(scrollState.maxValue) }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .horizontalScroll(scrollState)
                    .align(Alignment.Center),
                horizontalArrangement = Arrangement.End,
            ) {
                AutoSizeText(
                    textAlign = TextAlign.End,
                    text = infix,
                )

                Spacer(modifier = Modifier.width(Tinny))

                Cursor()
            }

            val resultTextColor = if (resultErrorMessage != null) {
                MaterialTheme.colorScheme.error
            } else {
                Color.Unspecified
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .align(Alignment.BottomEnd),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.displaySmall,
                color = resultTextColor,
                text = resultErrorMessage ?: result,
            )
        }
    }
}

@Composable
private fun Cursor() {
    val infiniteTransition = rememberInfiniteTransition()
    val cursorColor by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.primary,
        targetValue = Color.Transparent,
        animationSpec = infiniteRepeatable(
            animation = tween(CURSOR_ANIMATION_DURATION),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        modifier = Modifier
            .width(2.dp)
            .fillMaxHeight()
            .background(color = cursorColor)
    )
}

@Composable
private fun OperatorsBox(
    modifier: Modifier,
    infix: MutableState<String>,
) {
    OperatorsBox(
        modifier = modifier,
        onDigitClicked = { digit ->
            infix.value = processNewInfix(infix.value, digit.digitToChar())
        },
        onCleanClicked = { infix.value = "" },
        onOpenParenthesesClicked = {
            infix.value = processNewInfix(infix.value, OPEN_PARENTHESES)
        },
        onCloseParenthesesClicked = {
            infix.value = processNewInfix(infix.value, CLOSE_PARENTHESES)
        },
        onDivideClicked = {
            infix.value = processNewInfix(infix.value, DIVIDE)
        },
        onMultiplyClicked = {
            infix.value = processNewInfix(infix.value, MULTIPLY)
        },
        onSubtractClicked = {
            infix.value = processNewInfix(infix.value, SUBTRACT)
        },
        onAddClicked = {
            infix.value = processNewInfix(infix.value, ADD)
        },
        onDecimalClicked = {
            infix.value = processNewInfix(infix.value, AMOUNT_DELIMITER)
        },
        onRemoveClicked = {
            infix.value = infix.value.dropLast(1)
        },
    )
}

@Composable
private fun OperatorsBox(
    modifier: Modifier,
    onDigitClicked: (Int) -> Unit,
    onCleanClicked: () -> Unit,
    onOpenParenthesesClicked: () -> Unit,
    onCloseParenthesesClicked: () -> Unit,
    onDivideClicked: () -> Unit,
    onMultiplyClicked: () -> Unit,
    onSubtractClicked: () -> Unit,
    onAddClicked: () -> Unit,
    onDecimalClicked: () -> Unit,
    onRemoveClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(vertical = Normal)
    ) {
        PadRow(
            text1 = CLEAN_STRING,
            text2 = OPEN_PARENTHESES_STRING,
            text3 = CLOSE_PARENTHESES_STRING,
            text4 = DIVIDE_STRING,
            onClick1 = onCleanClicked,
            onClick2 = onOpenParenthesesClicked,
            onClick3 = onCloseParenthesesClicked,
            onClick4 = onDivideClicked,
        )

        Spacer(modifier = Modifier.height(Medium))

        PadRow(
            text1 = "7",
            text2 = "8",
            text3 = "9",
            text4 = MULTIPLY_STRING,
            onClick1 = { onDigitClicked(7) },
            onClick2 = { onDigitClicked(8) },
            onClick3 = { onDigitClicked(9) },
            onClick4 = onMultiplyClicked,
        )

        Spacer(modifier = Modifier.height(Medium))

        PadRow(
            text1 = "4",
            text2 = "5",
            text3 = "6",
            text4 = SUBTRACT_STRING,
            onClick1 = { onDigitClicked(4) },
            onClick2 = { onDigitClicked(5) },
            onClick3 = { onDigitClicked(6) },
            onClick4 = onSubtractClicked,
        )

        Spacer(modifier = Modifier.height(Medium))

        PadRow(
            text1 = "1",
            text2 = "2",
            text3 = "3",
            text4 = ADD_STRING,
            onClick1 = { onDigitClicked(1) },
            onClick2 = { onDigitClicked(2) },
            onClick3 = { onDigitClicked(3) },
            onClick4 = onAddClicked,
        )

        Spacer(modifier = Modifier.height(Medium))

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Pad(text = "0", weight = 2f, onClick = { onDigitClicked(0) })
            Spacer(modifier = Modifier.width(Medium))
            Pad(text = AMOUNT_DELIMITER_STRING, onClick = onDecimalClicked)
            Spacer(modifier = Modifier.width(Medium))
            Pad(
                text = "",
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = onRemoveClicked,
            ) {
                Icon(
                    modifier = Modifier.padding(vertical = Medium),
                    imageVector = Icons.AutoMirrored.Rounded.Backspace,
                    tint = Color.White,
                    contentDescription = stringResource(Resources.string.remove)
                )
            }
        }
    }
}

@Composable
private fun PadRow(
    text1: String,
    text2: String,
    text3: String,
    text4: String,
    onClick1: () -> Unit,
    onClick2: () -> Unit,
    onClick3: () -> Unit,
    onClick4: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Pad(text = text1, onClick = onClick1)
        Spacer(modifier = Modifier.width(Medium))
        Pad(text = text2, onClick = onClick2)
        Spacer(modifier = Modifier.width(Medium))
        Pad(text = text3, onClick = onClick3)
        Spacer(modifier = Modifier.width(Medium))
        Pad(
            text = text4,
            onClick = onClick4,
            contentColor = Color.White,
            containerColor = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun RowScope.Pad(
    text: String,
    weight: Float = 1f,
    containerColor: Color = MaterialTheme.colorScheme.secondary,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    onClick: () -> Unit,
    content: (@Composable () -> Unit)? = null,
) {
    DsButton(
        modifier = Modifier.weight(weight, fill = true),
        colors = ButtonDefaults.buttonColors()
            .copy(
                containerColor = containerColor,
                contentColor = contentColor,
            ),
        onClick = onClick
    ) {
        content?.invoke() ?: run {
            Text(
                modifier = Modifier.padding(vertical = Medium),
                style = MaterialTheme.typography.titleLarge,
                text = text,
            )
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
private fun AutoSizeText(
    modifier: Modifier = Modifier,
    text: String,
    textAlign: TextAlign,
    maxFontSize: TextUnit = MAX_INFIX_TEXT_SIZE,
    minFontSize: TextUnit = MIN_INFIX_TEXT_SIZE,
) {
    var fontSize by remember { mutableStateOf(maxFontSize) }
    val paint = remember { android.text.TextPaint() }
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val maxWidthPx = remember {
        with(density) { configuration.screenWidthDp.dp.toPx() * 0.75 }
    }

    LaunchedEffect(text) {
        var low = minFontSize.value
        var high = maxFontSize.value

        while (high - low > 0.5f) {
            val mid = (low + high) / 2f
            paint.textSize = with(density) { mid.sp.toPx() }
            val width = paint.measureText(text)
            if (width <= maxWidthPx) {
                low = mid
            } else {
                high = mid
            }
        }

        fontSize = low.sp
    }

    val animatedFontSize by animateFloatAsState(
        targetValue = fontSize.value,
        animationSpec = tween(durationMillis = AUTO_FONT_SIZE_ANIMATION_DURATION),
        label = "autoSizeAnimation"
    )

    Text(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        fontSize = animatedFontSize.sp,
    )
}

// todo: find (think of) a better approach
@Suppress("All")
private fun processNewInfix(current: String, newValue: Char): String {
    if (current.isEmpty() &&
        (
            newValue == DIVIDE ||
                newValue == MULTIPLY ||
                newValue == ADD ||
                newValue == AMOUNT_DELIMITER ||
                newValue == CLOSE_PARENTHESES
            )
    ) {
        return ""
    }

    if (current.isEmpty()) return current + newValue

    if (current.last() == AMOUNT_DELIMITER && !newValue.isDigit()) return current

    if (current.last().isOperand() && newValue.isOperand()) return current

    if (current.last() == CLOSE_PARENTHESES && newValue == OPEN_PARENTHESES) return current

    if ((
            !current.last()
                .isOperand() && current.last() != OPEN_PARENTHESES
            ) && newValue == OPEN_PARENTHESES
    ) {
        return current
    }

    return current + newValue
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun CalculatorContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Content(
                modifier = Modifier.fillMaxSize(),
                state = CalculatorState(
                    infix = "3+4*2/(1-5)",
                    result = "5",
                    errorMessage = "Invalid expression"
                ),
                onInfixInputChanged = { _ -> },
                onApplyClicked = {},
            )
        }
    }
}
