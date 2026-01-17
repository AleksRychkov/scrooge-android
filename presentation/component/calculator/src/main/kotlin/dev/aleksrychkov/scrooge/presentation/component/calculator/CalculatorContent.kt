package dev.aleksrychkov.scrooge.presentation.component.calculator

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Backspace
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsButton
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsSecondaryCard
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Medium
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.presentation.component.calculator.internal.CalculatorComponentInternal
import dev.aleksrychkov.scrooge.presentation.component.calculator.internal.CalculatorState
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Suppress("UnusedParameter")
@Composable
fun CalculatorContent(
    modifier: Modifier,
    component: CalculatorComponent,
    callback: (String) -> Unit,
) {
    CalculatorContent(
        modifier = modifier,
        component = component as CalculatorComponentInternal,
    )
}

@Composable
private fun CalculatorContent(
    modifier: Modifier,
    component: CalculatorComponentInternal,
) {
    val state by component.state.collectAsStateWithLifecycle()
    Content(
        modifier = modifier,
        state = state,
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    state: CalculatorState,
) {
    Column(
        modifier = modifier.padding(Large)
    ) {
        InputBox(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1f, fill = true),
            infix = state.infix,
            result = state.result,
        )

        OperatorsBox(
            modifier = Modifier.fillMaxWidth()
        )

        ExtendedFloatingActionButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Normal),
            onClick = {},
        ) {
            Text(stringResource(Resources.string.apply))
        }
    }
}

@Composable
private fun InputBox(
    modifier: Modifier,
    infix: String,
    result: String,
) {
    DsSecondaryCard(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(Large)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .align(Alignment.Center),
                autoSize = TextAutoSize.StepBased(
                    minFontSize = 36.sp,
                    maxFontSize = 60.sp,
                ),
                textAlign = TextAlign.End,
                maxLines = 1,
                text = infix,
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomEnd),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.displaySmall,
                text = result,
            )
        }
    }
}

@Composable
private fun OperatorsBox(
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .padding(vertical = Normal)
    ) {
        PadRow(
            text1 = "C",
            text2 = "(",
            text3 = ")",
            text4 = "÷",
            onClick1 = {},
            onClick2 = {},
            onClick3 = {},
            onClick4 = {},
        )

        Spacer(modifier = Modifier.height(Medium))

        PadRow(
            text1 = "7",
            text2 = "8",
            text3 = "9",
            text4 = "×",
            onClick1 = {},
            onClick2 = {},
            onClick3 = {},
            onClick4 = {},
        )

        Spacer(modifier = Modifier.height(Medium))

        PadRow(
            text1 = "4",
            text2 = "5",
            text3 = "6",
            text4 = "—",
            onClick1 = {},
            onClick2 = {},
            onClick3 = {},
            onClick4 = {},
        )

        Spacer(modifier = Modifier.height(Medium))

        PadRow(
            text1 = "1",
            text2 = "2",
            text3 = "3",
            text4 = "+",
            onClick1 = {},
            onClick2 = {},
            onClick3 = {},
            onClick4 = {},
        )

        Spacer(modifier = Modifier.height(Medium))

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Pad(text = "0", weight = 2f, onClick = {})
            Spacer(modifier = Modifier.width(Medium))
            Pad(text = ",", onClick = {})
            Spacer(modifier = Modifier.width(Medium))
            Pad(
                text = "",
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {},
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
                ),
            )
        }
    }
}
