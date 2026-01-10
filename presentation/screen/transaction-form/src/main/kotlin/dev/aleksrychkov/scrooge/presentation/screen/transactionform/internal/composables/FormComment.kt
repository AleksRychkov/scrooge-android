package dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
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
import kotlinx.coroutines.flow.collectLatest
import dev.aleksrychkov.scrooge.core.resources.R as Resources

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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val focusRequester = remember { FocusRequester() }
            val focusManager = LocalFocusManager.current

            if (isEditing && isLoading) return@Row

            val amountTextFieldState = rememberTextFieldState("")
            LaunchedEffect(key1 = comment) {
                if (comment != amountTextFieldState.text.toString()) {
                    amountTextFieldState.setTextAndPlaceCursorAtEnd(comment)
                }
            }
            LaunchedEffect(amountTextFieldState) {
                snapshotFlow { amountTextFieldState.text.toString() }
                    .collectLatest {
                        onCommentChanged(it)
                    }
            }

            TextField(
                modifier = Modifier
                    .weight(weight = 1f, fill = true)
                    .focusRequester(focusRequester),
                state = amountTextFieldState,
                label = {
                    Text(text = stringResource(Resources.string.form_comment))
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
                    if (amountTextFieldState.text.isNotBlank()) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(Resources.string.clear),
                            modifier = Modifier
                                .clickable { amountTextFieldState.setTextAndPlaceCursorAtEnd("") },
                        )
                    }
                },
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
