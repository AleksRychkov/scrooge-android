package dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsSecondaryCard
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal2X

@Composable
internal fun FormCurrency(
    modifier: Modifier = Modifier,
    currencySymbol: String,
    onCurrencyClicked: () -> Unit,
) {
    DsSecondaryCard(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .debounceClickable(onClick = onCurrencyClicked)
                .padding(
                    horizontal = Normal2X
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                style = MaterialTheme.typography.titleMedium,
                text = currencySymbol,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun FormCurrencyPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            FormCurrency(
                modifier = Modifier,
                currencySymbol = "$",
                onCurrencyClicked = {},
            )
        }
    }
}
