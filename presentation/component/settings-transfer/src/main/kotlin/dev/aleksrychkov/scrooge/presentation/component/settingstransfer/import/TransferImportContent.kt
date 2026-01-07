package dev.aleksrychkov.scrooge.presentation.component.settingstransfer.import

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.presentation.component.settingstransfer.import.internal.TransferImportComponentInternal
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun TransferImportContent(
    modifier: Modifier,
    component: TransferImportComponent,
) {
    TransferImportContent(
        modifier = modifier,
        component = component as TransferImportComponentInternal,
    )
}

@Composable
private fun TransferImportContent(
    modifier: Modifier,
    component: TransferImportComponentInternal,
) {
    TransferImportContent(
        modifier = modifier,
        onImportClicked = component::import,
    )
}

@Composable
private fun TransferImportContent(
    modifier: Modifier,
    onImportClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = CardDefaults.shape,
            )
            .clip(shape = CardDefaults.shape)
            .debounceClickable(onClick = onImportClicked)
            .padding(Normal),
    ) {
        Text(
            text = stringResource(Resources.string.transfer_import_title),
            style = MaterialTheme.typography.titleMedium,
        )

        Text(
            modifier = Modifier.padding(top = Normal),
            text = stringResource(Resources.string.transfer_import_description),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun TransferImportContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            TransferImportContent(
                modifier = Modifier.fillMaxWidth(),
                onImportClicked = {},
            )
        }
    }
}
