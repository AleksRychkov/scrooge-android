package dev.aleksrychkov.scrooge.dev.aleksrychkov.scrooge.presentation.screen.root.internal.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsPacmanIndicator
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal2X
import dev.aleksrychkov.scrooge.core.entity.TransferStateEntity
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun TransferContent(
    modifier: Modifier,
    transferState: TransferStateEntity,
) {
    Box(
        modifier = modifier.padding(Large),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val text = when (transferState.current) {
                is TransferStateEntity.State.Importing ->
                    stringResource(Resources.string.transfer_import_in_progress)

                is TransferStateEntity.State.Exporting ->
                    stringResource(Resources.string.transfer_export_in_progress)

                else -> "todo"
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(Normal2X))
            DsPacmanIndicator(
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun TransferContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            TransferContent(
                modifier = Modifier.fillMaxSize(),
                transferState = TransferStateEntity(
                    current = TransferStateEntity.State.Importing(),
                ),
            )
        }
    }
}
