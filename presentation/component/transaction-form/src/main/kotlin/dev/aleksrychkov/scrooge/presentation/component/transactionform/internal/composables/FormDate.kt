package dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsSecondaryCard
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.picker.WheelDatePickerDialog
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.entity.Datestamp

@Composable
internal fun FormDate(
    modifier: Modifier,
    datestamp: Datestamp,
    datestampReadable: String,
    onDateSelected: (Long?) -> Unit,
) {
    var showModal by remember { mutableStateOf(false) }
    DsSecondaryCard(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .debounceClickable { showModal = true }
                .padding(Large)
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(Normal))
            Text(
                text = datestampReadable
            )
        }
    }
    if (showModal) {
        val timestamp = remember(datestamp) { datestamp.toInstant() }
        WheelDatePickerDialog(
            timestamp = timestamp,
            onDateSelected = onDateSelected,
            onDismiss = { showModal = false }
        )
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun FormDatePreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            FormDate(
                modifier = Modifier,
                datestamp = Datestamp.now(),
                datestampReadable = "Today",
                onDateSelected = { _ -> },
            )
        }
    }
}
