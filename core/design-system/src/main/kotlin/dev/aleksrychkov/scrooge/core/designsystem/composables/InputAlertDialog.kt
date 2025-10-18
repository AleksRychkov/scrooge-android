package dev.aleksrychkov.scrooge.core.designsystem.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Suppress("LongParameterList")
@Composable
fun InputAlertDialog(
    value: String,
    singleLine: Boolean = true,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
    onValueChanged: (String) -> Unit,
    title: @Composable (() -> Unit)? = null,
    confirm: @Composable RowScope.() -> Unit,
    dismiss: @Composable RowScope.() -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onConfirmRequest
            ) {
                confirm()
            }
        },
        title = title,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                dismiss()
            }
        },
        text = {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = value,
                singleLine = singleLine,
                minLines = minLines,
                keyboardOptions = keyboardOptions,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(Resources.string.clear),
                        modifier = Modifier
                            .clickable { onValueChanged("") },
                    )
                },
                onValueChange = onValueChanged,
            )
        }
    )
}
