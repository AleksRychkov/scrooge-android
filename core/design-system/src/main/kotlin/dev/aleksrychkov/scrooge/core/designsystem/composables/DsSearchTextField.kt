package dev.aleksrychkov.scrooge.core.designsystem.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
fun DsSearchTextField(
    modifier: Modifier,
    value: String,
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
    TextField(
        modifier = modifier
            .background(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.secondary,
            ),
        value = text,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
            )
        },
        trailingIcon = {
            if (text.isNotBlank()) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = stringResource(Resources.string.clear),
                    modifier = Modifier
                        .clip(CircleShape)
                        .debounceClickable { text = "" },
                )
            }
        },
        onValueChange = {
            text = it
        },
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            unfocusedTextColor = Color.Unspecified,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
    )
}
