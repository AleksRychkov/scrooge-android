package dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsInputTextFieldsColors
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsSecondaryCard
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.entity.AMOUNT_DELIMITER
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.utils.AmountInputTransformation
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.utils.AmountOutputTransformation
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Suppress("MagicNumber")
@OptIn(FlowPreview::class)
@Composable
internal fun FormAmount(
    modifier: Modifier,
    isEditing: Boolean,
    isLoading: Boolean,
    amount: String,
    currency: String,
    amountChanged: (String) -> Unit,
    openCurrencyModal: () -> Unit,
) {
    DsSecondaryCard(
        modifier = modifier.height(intrinsicSize = IntrinsicSize.Max),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val focusRequester = remember { FocusRequester() }
            val focusManager = LocalFocusManager.current

            if (isEditing && isLoading) return@Row

            val amountTextFieldState = rememberTextFieldState("")
            LaunchedEffect(key1 = amount) {
                if (amount != amountTextFieldState.text.toString()) {
                    amountTextFieldState.setTextAndPlaceCursorAtEnd(amount)
                }
            }
            LaunchedEffect(amountTextFieldState) {
                snapshotFlow { amountTextFieldState.text.toString() }
                    .collectLatest {
                        amountChanged(it)
                    }
            }
            if (!isEditing) {
                LaunchedEffect(key1 = Unit) {
                    focusRequester.requestFocus()
                }
            }
            val outputTransformation = remember(key1 = currency) {
                AmountOutputTransformation(currency)
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
                    val placeholder = "$currency 0${AMOUNT_DELIMITER}00"
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
                modifier = Modifier.height(IntrinsicSize.Max),
                onClick = openCurrencyModal,
            ) {
                Text(
                    text = currency,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun FormContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            FormAmount(
                modifier = Modifier,
                amount = "12312,00",
                isEditing = false,
                isLoading = false,
                currency = CurrencyEntity.RUB.currencySymbol,
                amountChanged = {},
                openCurrencyModal = {},
            )
        }
    }
}
