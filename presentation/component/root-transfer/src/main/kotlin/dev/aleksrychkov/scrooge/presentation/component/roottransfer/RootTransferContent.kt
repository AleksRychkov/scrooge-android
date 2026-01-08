package dev.aleksrychkov.scrooge.presentation.component.roottransfer

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsButton
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsPacmanIndicator
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal2X
import dev.aleksrychkov.scrooge.core.designsystem.theme.Small
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
    val close = {
        setState(TransferStateEntity.State.None())
    }
    AnimatedContent(
        targetState = state.current,
        transitionSpec = {
            fadeIn(tween(durationMillis = 500))
                .togetherWith(fadeOut(tween(durationMillis = 500)))
        },
    ) { state ->
        Box(
            modifier = modifier.padding(Large),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                when (state) {
                    is TransferStateEntity.State.Exporting -> InProgress(state)
                    is TransferStateEntity.State.Importing -> InProgress(state)
                    is TransferStateEntity.State.ExportingFailed -> Failed(state, close)
                    is TransferStateEntity.State.ImportingFailed -> Failed(state, close)
                    is TransferStateEntity.State.ExportingSuccess -> SuccessExport(state, close)
                    is TransferStateEntity.State.ImportingSuccess -> SuccessImport(close)
                    is TransferStateEntity.State.None -> Box(modifier = Modifier)
                }
            }
        }
    }
}

@Composable
private fun InProgress(
    state: TransferStateEntity.State,
) {
    val text = if (state is TransferStateEntity.State.Importing) {
        stringResource(Resources.string.transfer_import_in_progress)
    } else {
        stringResource(Resources.string.transfer_export_in_progress)
    }
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
    )
    Spacer(modifier = Modifier.height(Normal2X))
    DsPacmanIndicator(
        color = MaterialTheme.colorScheme.onBackground,
        ballIn = state == TransferStateEntity.State.Importing,
    )
}

@Composable
private fun Failed(
    state: TransferStateEntity.State,
    close: () -> Unit,
) {
    Text(
        text = stringResource(Resources.string.transfer_transfer_failed),
        style = MaterialTheme.typography.titleMedium,
    )
    state.info?.let {
        Spacer(modifier = Modifier.height(Normal2X))
        Text(
            text = it,
        )
    }
    Spacer(modifier = Modifier.height(Normal2X))
    DsButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = close,
    ) {
        Text(text = stringResource(Resources.string.close))
    }
}

@Composable
private fun SuccessExport(
    state: TransferStateEntity.State.ExportingSuccess,
    close: () -> Unit,
) {
    Text(
        text = stringResource(Resources.string.transfer_transfer_success),
        style = MaterialTheme.typography.titleMedium,
    )

    Spacer(modifier = Modifier.height(Normal2X))
    val shareTxt = stringResource(Resources.string.send)
    val ctx = LocalContext.current
    TextButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "*/*"
                putExtra(Intent.EXTRA_STREAM, state.info)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            ctx.startActivity(Intent.createChooser(intent, shareTxt))
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Rounded.Share,
                contentDescription = shareTxt,
            )
            Text(
                modifier = Modifier.padding(start = Small),
                text = shareTxt,
            )
        }
    }

    Spacer(modifier = Modifier.height(Normal2X))
    DsButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = close,
    ) {
        Text(text = stringResource(Resources.string.close))
    }
}

@Composable
private fun SuccessImport(
    close: () -> Unit,
) {
    Text(
        text = stringResource(Resources.string.transfer_transfer_success),
        style = MaterialTheme.typography.titleMedium,
    )

    Spacer(modifier = Modifier.height(Normal2X))
    DsButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = close,
    ) {
        Text(text = stringResource(Resources.string.close))
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
