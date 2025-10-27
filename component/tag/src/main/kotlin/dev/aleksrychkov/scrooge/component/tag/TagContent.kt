package dev.aleksrychkov.scrooge.component.tag

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.aleksrychkov.scrooge.component.tag.internal.TagComponentInternal
import dev.aleksrychkov.scrooge.component.tag.internal.udf.TagState
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun TagContent(
    modifier: Modifier,
    component: TagComponent,
    callback: (TagEntity?) -> Unit,
) {
    TagContent(
        modifier = modifier,
        component = component as TagComponentInternal,
        callback = callback,
    )
}

@Suppress("UnusedParameter")
@Composable
private fun TagContent(
    modifier: Modifier,
    component: TagComponentInternal,
    callback: (TagEntity?) -> Unit,
) {
    Scaffold(
        modifier = modifier,
    ) { innerPadding ->
        TagContent(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        )
    }
}

@Composable
private fun TagContent(
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .displayCutoutPadding()
            .statusBarsPadding()
    ) {
        TagBar(
            modifier = Modifier.fillMaxWidth(),
            state = TagState(),
            setSearchQuery = {},
            addTagClicked = {},
        )
    }
}

@Composable
private fun TagBar(
    modifier: Modifier,
    state: TagState,
    setSearchQuery: (String) -> Unit,
    addTagClicked: () -> Unit,
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .padding(start = Normal)
                .weight(weight = 1f, fill = true),
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.searchQuery,
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                trailingIcon = {
                    if (state.searchQuery.isNotBlank()) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(Resources.string.clear),
                            modifier = Modifier
                                .clickable { setSearchQuery("") },
                        )
                    }
                },
                onValueChange = setSearchQuery,
            )
        }

        Box(
            modifier = Modifier
                .height(60.dp)
                .aspectRatio(1f)
                .debounceClickable(onClick = addTagClicked),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = stringResource(Resources.string.tag_add),
            )
        }
    }
}
