package dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.composables

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.aleksrychkov.scrooge.core.designsystem.composables.animateElevation
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppBarShadow
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.TransactionFormComponentInternal
import dev.aleksrychkov.scrooge.core.resources.R as Resources

const val MINIMAL_SCROLL_VALUE_TO_CAST_SHADOW = 10

@Composable
internal fun FormTopAppBar(
    component: TransactionFormComponentInternal,
    scrollState: ScrollState,
    transactionId: Long?,
    transactionType: TransactionType,
) {
    val headerElevation by remember {
        derivedStateOf {
            if (scrollState.value > MINIMAL_SCROLL_VALUE_TO_CAST_SHADOW) {
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
        FormTopAppBar(
            isEditing = transactionId != null,
            type = transactionType,
            onBackClicked = component::onBackClicked,
            onSaveClicked = component::onSaveClicked,
            onDeleteClicked = component::onDeleteClicked,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormTopAppBar(
    isEditing: Boolean,
    type: TransactionType,
    onBackClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
) {
    val title: String

    when (type) {
        TransactionType.Income -> {
            title = if (isEditing) {
                stringResource(Resources.string.form_edit_income)
            } else {
                stringResource(Resources.string.add_income)
            }
        }

        TransactionType.Expense -> {
            title = if (isEditing) {
                stringResource(Resources.string.form_edit_expense)
            } else {
                stringResource(Resources.string.add_expense)
            }
        }
    }

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = title)
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(Resources.string.back),
                )
            }
        },
        actions = {
            FormDeleteTransaction(
                isEditing = isEditing,
                onDeleteClicked = onDeleteClicked,
            )
            IconButton(onClick = onSaveClicked) {
                Icon(
                    imageVector = Icons.Rounded.Save,
                    contentDescription = stringResource(Resources.string.save),
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FormDeleteTransaction(
    isEditing: Boolean,
    onDeleteClicked: () -> Unit,
) {
    if (!isEditing) return

    val showModal = remember { mutableStateOf(false) }
    IconButton(
        onClick = {
            showModal.value = true
        }
    ) {
        Icon(
            imageVector = Icons.Rounded.Delete,
            contentDescription = stringResource(Resources.string.delete),
        )
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
