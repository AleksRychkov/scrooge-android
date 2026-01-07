package dev.aleksrychkov.scrooge.presentation.component.settingstransfer.export

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
import dev.aleksrychkov.scrooge.presentation.component.settingstransfer.export.internal.TransferExportComponentInternal
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun TransferExportContent(
    modifier: Modifier,
    component: TransferExportComponent,
) {
    TransferExportContent(
        modifier = modifier,
        component = component as TransferExportComponentInternal,
    )
}

@Composable
private fun TransferExportContent(
    modifier: Modifier,
    component: TransferExportComponentInternal,
) {
    TransferExportContent(
        modifier = modifier,
        onExportClicked = component::export,
    )
}

@Composable
private fun TransferExportContent(
    modifier: Modifier,
    onExportClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = CardDefaults.shape,
            )
            .clip(shape = CardDefaults.shape)
            .debounceClickable(onClick = onExportClicked)
            .padding(Normal),
    ) {
        Text(
            text = stringResource(Resources.string.transfer_export_title),
            style = MaterialTheme.typography.titleMedium,
        )

        Text(
            modifier = Modifier.padding(top = Normal),
            text = stringResource(Resources.string.transfer_export_description),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun TransferExportContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            TransferExportContent(
                modifier = Modifier.fillMaxWidth(),
                onExportClicked = {},
            )
        }
    }
}
