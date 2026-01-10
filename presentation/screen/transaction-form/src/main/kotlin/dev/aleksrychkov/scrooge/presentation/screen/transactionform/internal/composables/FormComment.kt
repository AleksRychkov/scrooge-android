package dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsSecondaryCard
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsTextField
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
internal fun FormComment(
    modifier: Modifier,
    comment: String?,
    onCommentChanged: (String) -> Unit,
) {
    DsSecondaryCard(modifier = modifier.height(intrinsicSize = IntrinsicSize.Max)) {
        DsTextField(
            modifier = Modifier.fillMaxWidth(),
            value = comment ?: "",
            singleLine = false,
            label = {
                Text(text = stringResource(Resources.string.form_comment))
            },
            onValueChanged = onCommentChanged,
        )
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun FormCommentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            FormComment(
                modifier = Modifier.fillMaxWidth(),
                comment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
                    " Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                    " Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris" +
                    " nisi ut aliquip ex ea commodo consequat.",
                onCommentChanged = { _ -> },
            )
        }
    }
}
