package dev.aleksrychkov.scrooge.component.transactionform.internal.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.component.transactionform.internal.TransactionFormComponentInternal
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
internal fun FormTopAppBar(
    component: TransactionFormComponentInternal,
) {
    val state by component.state.collectAsStateWithLifecycle()

    FormTopAppBar(
        isEditing = state.transactionId != null,
        type = state.transactionType,
        onBackClicked = component::onBackClicked,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormTopAppBar(
    isEditing: Boolean,
    type: TransactionType,
    onBackClicked: () -> Unit,
) {
    val title: String

    when (type) {
        TransactionType.Income -> {
            title = if (isEditing) {
                stringResource(Resources.string.form_edit_income)
            } else {
                stringResource(Resources.string.form_add_income)
            }
        }

        TransactionType.Expense -> {
            title = if (isEditing) {
                stringResource(Resources.string.form_edit_expense)
            } else {
                stringResource(Resources.string.form_add_expense)
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
    )
}
