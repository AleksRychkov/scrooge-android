package dev.aleksrychkov.scrooge.component.transactionform.internal.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.aleksrychkov.scrooge.component.transactionform.internal.TransactionFormComponentInternal
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FormTopAppBar(
    component: TransactionFormComponentInternal,
) {
    TopAppBar(
        title = {
            Text(text = stringResource(Resources.string.form_add_transaction))
        },
        navigationIcon = {
            IconButton(onClick = component::onBackClicked) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(Resources.string.back),
                )
            }
        },
        actions = {
            TextButton(onClick = component::onSubmitClicked) {
                Text(text = stringResource(Resources.string.submit))
            }
        }
    )
}
