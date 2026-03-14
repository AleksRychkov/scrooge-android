package dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Backspace
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsButton
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Small
import dev.aleksrychkov.scrooge.core.designsystem.theme.Tinny
import kotlin.time.Duration.Companion.milliseconds
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
internal fun NumPad(
    modifier: Modifier,
    append: (String) -> Unit,
    remove: () -> Unit,
) {
    Column(modifier = modifier) {
        PadRow(
            text1 = "7",
            text2 = "8",
            text3 = "9",
            append = append,
        )
        Spacer(modifier = Modifier.height(Small))
        PadRow(
            text1 = "4",
            text2 = "5",
            text3 = "6",
            append = append,
        )
        Spacer(modifier = Modifier.height(Small))
        PadRow(
            text1 = "1",
            text2 = "2",
            text3 = "3",
            append = append,
        )
        Spacer(modifier = Modifier.height(Small))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Pad(
                text = ".",
                containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                onClick = { append(".") },
            )
            Spacer(modifier = Modifier.width(Small))
            Pad(
                text = "0",
                onClick = { append(".") },
            )
            Spacer(modifier = Modifier.width(Small))
            Pad(
                modifier = Modifier.fillMaxHeight(),
                text = "",
                containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                content = {
                    Icon(
                        modifier = Modifier.padding(),
                        imageVector = Icons.AutoMirrored.Rounded.Backspace,
                        contentDescription = stringResource(Resources.string.remove)
                    )
                },
                onClick = { remove() }
            )
        }
    }
}

@Composable
private fun PadRow(
    modifier: Modifier = Modifier,
    text1: String,
    text2: String,
    text3: String,
    append: (String) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Pad(text = text1, onClick = { append(text1) })
        Spacer(modifier = Modifier.width(Small))
        Pad(text = text2, onClick = { append(text2) })
        Spacer(modifier = Modifier.width(Small))
        Pad(text = text3, onClick = { append(text3) })
    }
}

@Composable
private fun RowScope.Pad(
    modifier: Modifier = Modifier,
    text: String,
    weight: Float = 1f,
    containerColor: Color = MaterialTheme.colorScheme.secondary,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    onClick: () -> Unit,
    content: (@Composable () -> Unit)? = null,
) {
    DsButton(
        modifier = modifier.weight(weight, fill = true),
        colors = ButtonDefaults.buttonColors()
            .copy(
                containerColor = containerColor,
                contentColor = contentColor,
            ),
        debounce = 500.milliseconds,
        onClick = onClick
    ) {
        content?.invoke() ?: run {
            Text(
                modifier = Modifier.padding(vertical = Tinny),
                style = MaterialTheme.typography.titleMedium,
                text = text,
            )
        }
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun NumPadPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            NumPad(
                modifier = Modifier.fillMaxWidth(),
                append = { _ -> },
                remove = {},
            )
        }
    }
}
