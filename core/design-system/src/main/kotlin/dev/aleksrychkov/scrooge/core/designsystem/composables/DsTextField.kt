package dev.aleksrychkov.scrooge.core.designsystem.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@OptIn(FlowPreview::class)
@Suppress("MagicNumber")
@Composable
fun DsTextField(
    modifier: Modifier,
    value: String,
    singleLine: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    label: @Composable (() -> Unit)? = null,
    onValueChanged: (String) -> Unit,
) {
    var text by remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = Unit) {
        text = value
        snapshotFlow { text }
            .debounce(300L)
            .distinctUntilChanged()
            .onEach {
                onValueChanged(it)
            }
            .launchIn(this)
    }
    val focusManager = LocalFocusManager.current
    TextField(
        modifier = modifier
            .background(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.secondary,
            ),
        value = text,
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text,
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = {
            if (text.isNotBlank()) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = stringResource(Resources.string.clear),
                    modifier = Modifier
                        .clickable { text = "" },
                )
            }
        },
        onValueChange = {
            text = it
        },
        colors = DsInputTextFieldsColors(),
    )
}
