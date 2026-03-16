package dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FormDeleteTransaction(
    modifier: Modifier = Modifier,
    transactionId: Long? = null,
    onDeleteClicked: () -> Unit,
) {
    if (transactionId == null) {
        Box(modifier = modifier)
        return
    }
    val showModal = remember { mutableStateOf(false) }
    TextButton(
        modifier = modifier.padding(top = Normal),
        shape = MaterialTheme.shapes.large,
        onClick = {
            showModal.value = true
        }
    ) {
        Text(stringResource(Resources.string.delete))
    }

    if (!showModal.value) return
    AlertDialog(
        onDismissRequest = {
            showModal.value = false
        },
        text = {
            Text(
                text = stringResource(Resources.string.form_delete_confirmation_text)
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    showModal.value = false
                    onDeleteClicked()
                }
            ) {
                Text(text = stringResource(Resources.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    showModal.value = false
                }
            ) {
                Text(text = stringResource(Resources.string.dismiss))
            }
        },
    )
}
