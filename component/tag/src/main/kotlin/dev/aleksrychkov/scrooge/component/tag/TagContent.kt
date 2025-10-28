package dev.aleksrychkov.scrooge.component.tag

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.component.tag.internal.TagComponentInternal
import dev.aleksrychkov.scrooge.component.tag.internal.udf.TagEffect
import dev.aleksrychkov.scrooge.component.tag.internal.udf.TagState
import dev.aleksrychkov.scrooge.core.designsystem.composables.CountdownSnackbar
import dev.aleksrychkov.scrooge.core.designsystem.composables.DialogSnackbarHost
import dev.aleksrychkov.scrooge.core.designsystem.composables.NavigationBarSpacer
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.composables.showCountdownSnackbar
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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

@Composable
private fun TagContent(
    modifier: Modifier,
    component: TagComponentInternal,
    callback: (TagEntity?) -> Unit,
) {
    val state by component.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }
    DisposableEffect(component) {
        val job = scope.launch {
            component.effects
                .onEach { effect ->
                    when (effect) {
                        is TagEffect.ShowInfoMessage -> {
                            snackbarHostState.showCountdownSnackbar(message = effect.message)
                        }

                        is TagEffect.TagDeleted -> {
                            val result = snackbarHostState.showCountdownSnackbar(
                                message = effect.message,
                                actionLabel = effect.actionLabel,
                                useCountDown = true,
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                component.restoreTag(tag = effect.tag)
                            }
                        }
                    }
                }
                .collect()
        }
        onDispose {
            job.cancel()
        }
    }
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            DialogSnackbarHost(
                snackbarHostState = snackbarHostState,
                snackbar = { data ->
                    CountdownSnackbar(data)
                },
            )
        },
    ) { innerPadding ->
        TagContent(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            state = state,
            selectTag = callback,
            deleteTag = component::deleteTag,
            setSearchQuery = component::setSearchQuery,
            addNewTagClicked = component::addNewTag,
        )
    }
}

@Composable
private fun TagContent(
    modifier: Modifier,
    state: TagState,
    selectTag: (TagEntity) -> Unit,
    deleteTag: (TagEntity) -> Unit,
    setSearchQuery: (String) -> Unit,
    addNewTagClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .displayCutoutPadding()
            .statusBarsPadding()
    ) {
        TagBar(
            modifier = Modifier.fillMaxWidth(),
            state = state,
            setSearchQuery = setSearchQuery,
            addTagClicked = addNewTagClicked,
        )

        Spacer(modifier = Modifier.height(Normal))

        TagsList(
            modifier = Modifier.fillMaxSize(),
            state = state,
            selectTag = selectTag,
            deleteTag = deleteTag,
        )
    }
}

@Composable
private fun TagsList(
    modifier: Modifier,
    state: TagState,
    selectTag: (TagEntity) -> Unit,
    deleteTag: (TagEntity) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        val categories = if (state.searchQuery.isNotBlank()) {
            state.filtered
        } else {
            state.tags
        }

        items(
            items = categories,
            key = { category -> category.id }
        ) { tag ->
            Tag(
                modifier = Modifier.animateItem(),
                value = tag,
                selectTag = selectTag,
                deleteTag = deleteTag,
            )
        }

        item {
            NavigationBarSpacer()
        }
    }
}

@Composable
private fun Tag(
    modifier: Modifier = Modifier,
    value: TagEntity,
    selectTag: (TagEntity) -> Unit,
    deleteTag: (TagEntity) -> Unit,
) {
    val itemHeight = 60.dp
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = itemHeight)
            .clickable {
                selectTag(value)
            }
            .padding(start = Large),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .weight(weight = 1f, fill = true),
            text = value.name,
        )

        Box(
            modifier = Modifier
                .height(itemHeight)
                .aspectRatio(1f)
                .debounceClickable { deleteTag(value) },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = stringResource(Resources.string.tag_delete),
            )
        }
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
