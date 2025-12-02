package dev.aleksrychkov.scrooge.component.transactionform.internal.modal

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kotlin.time.Instant
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
internal fun DatePickerModal(
    timestamp: Instant,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = timestamp.toEpochMilliseconds(),
    )

    DatePickerDialog(
        colors = DatePickerDefaults.colors().copy(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text(stringResource(Resources.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Resources.string.close))
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors().copy(
                containerColor = MaterialTheme.colorScheme.background,
            )
        )
    }
}
