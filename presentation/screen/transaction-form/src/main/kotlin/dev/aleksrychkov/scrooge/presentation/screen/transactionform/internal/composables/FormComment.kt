package dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsInputTextFieldsColors
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsSecondaryCard
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Small
import kotlinx.coroutines.flow.collectLatest
import dev.aleksrychkov.scrooge.core.resources.R as Resources

private const val MAX_COMMENT_LENGTH = 100

@Composable
internal fun FormComment(
    modifier: Modifier,
    isEditing: Boolean,
    isLoading: Boolean,
    comment: String,
    onCommentChanged: (String) -> Unit,
) {
    DsSecondaryCard(modifier = modifier.height(intrinsicSize = IntrinsicSize.Max)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Small),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val focusRequester = remember { FocusRequester() }
            val focusManager = LocalFocusManager.current

            if (isEditing && isLoading) return@Row

            val commentTextFieldState = rememberTextFieldState("")
            LaunchedEffect(key1 = comment) {
                if (comment != commentTextFieldState.text.toString()) {
                    commentTextFieldState.setTextAndPlaceCursorAtEnd(comment)
                }
            }
            LaunchedEffect(commentTextFieldState) {
                snapshotFlow { commentTextFieldState.text.toString() }
                    .collectLatest {
                        onCommentChanged(it)
                    }
            }

            TextField(
                modifier = Modifier
                    .weight(weight = 1f, fill = true)
                    .focusRequester(focusRequester),
                state = commentTextFieldState,
                label = {
                    Text(text = stringResource(Resources.string.form_comment))
                },
                supportingText = {
                    Text(
                        modifier = Modifier.padding(bottom = Small),
                        text = "${commentTextFieldState.text.length} / $MAX_COMMENT_LENGTH",
                        style = MaterialTheme.typography.labelSmall,
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text,
                ),
                onKeyboardAction = {
                    focusManager.clearFocus()
                },
                colors = DsInputTextFieldsColors(),
                trailingIcon = {
                    if (commentTextFieldState.text.isNotBlank()) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(Resources.string.clear),
                            modifier = Modifier
                                .clickable { commentTextFieldState.setTextAndPlaceCursorAtEnd("") },
                        )
                    }
                },
                inputTransformation = InputTransformation.maxLength(maxLength = MAX_COMMENT_LENGTH),
            )
        }
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
                isEditing = false,
                isLoading = false,
                onCommentChanged = { _ -> },
            )
        }
    }
}
