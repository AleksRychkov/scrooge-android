package dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.composables

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.composables.animateElevation
import dev.aleksrychkov.scrooge.core.designsystem.theme.Medium
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.TransactionFormComponentInternal
import dev.aleksrychkov.scrooge.core.resources.R as Resources

const val MINIMAL_SCROLL_VALUE_TO_CAST_SHADOW = 10

@Composable
internal fun FormTopAppBar(
    component: TransactionFormComponentInternal,
    scrollState: ScrollState,
) {
    val headerElevation by remember {
        derivedStateOf {
            if (scrollState.value > MINIMAL_SCROLL_VALUE_TO_CAST_SHADOW) {
                Medium
            } else {
                0.dp
            }
        }
    }
    val animatedElevation by headerElevation.animateElevation()
    val state by component.state.collectAsStateWithLifecycle()

    Surface(
        Modifier.fillMaxWidth(),
        shadowElevation = animatedElevation,
    ) {
        FormTopAppBar(
            isEditing = state.transactionId != null,
            type = state.transactionType,
            onBackClicked = component::onBackClicked,
        )
    }
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
    )
}
