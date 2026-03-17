package dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsAutoSizeText
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity

@Composable
fun FormAmount(
    modifier: Modifier,
    amount: String,
    currencySymbol: String,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val (value, color) = if (amount.isBlank()) {
            "$currencySymbol 0,00" to MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f)
        } else {
            "$currencySymbol $amount" to MaterialTheme.colorScheme.onBackground
        }
        DsAutoSizeText(
            modifier = Modifier,
            text = value,
            color = color,
            maxFontSize = 48.sp,
            minFontSize = 24.sp,
        )
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun FormContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            FormAmount(
                modifier = Modifier.fillMaxWidth(),
                amount = "",
                currencySymbol = CurrencyEntity.RUB.currencySymbol,
            )
        }
    }
}
