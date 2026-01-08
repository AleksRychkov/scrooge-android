package dev.aleksrychkov.scrooge.presentation.component.roottransfer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsPacmanIndicator
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal2X
import dev.aleksrychkov.scrooge.core.entity.TransferStateEntity
import dev.aleksrychkov.scrooge.presentation.component.roottransfer.internal.RootTransferComponentInternal
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun RootTransferContent(
    modifier: Modifier,
    component: RootTransferComponent,
) {
    RootTransferContent(
        modifier = modifier,
        component = component as RootTransferComponentInternal,
    )
}

@Composable
private fun RootTransferContent(
    modifier: Modifier,
    component: RootTransferComponentInternal,
) {
    val state by component.state.collectAsStateWithLifecycle()

    RootTransferContent(
        modifier = modifier,
        state = state.state,
        setState = component::setState,
    )
}

@Composable
private fun RootTransferContent(
    modifier: Modifier,
    state: TransferStateEntity,
    setState: (TransferStateEntity.State) -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val text = when (state.current) {
                is TransferStateEntity.State.Importing ->
                    stringResource(Resources.string.transfer_import_in_progress)

                is TransferStateEntity.State.Exporting ->
                    stringResource(Resources.string.transfer_export_in_progress)

                is TransferStateEntity.State.ExportingFailed -> "export failed"
                is TransferStateEntity.State.ExportingSuccess -> "export success"
                is TransferStateEntity.State.ImportingFailed -> "import fail"
                is TransferStateEntity.State.None -> "none"
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(Normal2X))
            DsPacmanIndicator(
                color = MaterialTheme.colorScheme.onBackground,
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    setState(TransferStateEntity.State.None())
                }
            ) {
                Text("Close")
            }
        }
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun RootTransferContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            RootTransferContent(
                modifier = Modifier.fillMaxSize(),
                state = TransferStateEntity(
                    current = TransferStateEntity.State.Importing(),
                ),
                setState = { _ -> },
            )
        }
    }
}
