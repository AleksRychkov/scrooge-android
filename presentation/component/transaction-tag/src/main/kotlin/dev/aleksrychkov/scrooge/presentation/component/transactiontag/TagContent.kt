package dev.aleksrychkov.scrooge.presentation.component.transactiontag

import android.widget.Toast
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsButton
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsSearchTextField
import dev.aleksrychkov.scrooge.core.designsystem.composables.NavigationBarSpacer
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.ListItemHeight
import dev.aleksrychkov.scrooge.core.designsystem.theme.Medium
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.TagComponentInternal
import dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.TagEffect
import dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.TagState
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
    val context = LocalContext.current
    DisposableEffect(component) {
        val job = scope.launch {
            component.effects
                .onEach { effect ->
                    when (effect) {
                        is TagEffect.ShowInfoMessage -> {
                            Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
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
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
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
    Box(
        modifier = modifier.padding(bottom = Normal)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
        ) {
            val tags = if (state.searchQuery.isNotBlank()) {
                state.filtered
            } else {
                state.tags
            }

            items(
                items = tags,
                key = { tag -> tag.id }
            ) { tag ->
                Tag(
                    modifier = Modifier.animateItem(),
                    entity = tag,
                    isEditable = state.isEditable,
                    selectTag = selectTag,
                    deleteTag = deleteTag,
                )
            }

            item {
                NavigationBarSpacer()
            }
        }
    }
}

@Composable
private fun Tag(
    modifier: Modifier = Modifier,
    entity: TagEntity,
    isEditable: Boolean,
    selectTag: (TagEntity) -> Unit,
    deleteTag: (TagEntity) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = ListItemHeight)
            .clickable {
                selectTag(entity)
            }
            .padding(start = Large),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(weight = 1f, fill = true),
            text = entity.name,
        )
        if (!isEditable) return
        val isConfirmationAlertVisible = remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .height(ListItemHeight)
                .padding(Normal)
                .aspectRatio(1f)
                .clip(CircleShape)
                .debounceClickable {
                    isConfirmationAlertVisible.value = true
                }
                .padding(Medium),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                tint = MaterialTheme.colorScheme.error,
                contentDescription = stringResource(Resources.string.tag_delete),
            )
        }
        if (!isConfirmationAlertVisible.value) return
        AlertDialog(
            onDismissRequest = {
                isConfirmationAlertVisible.value = false
            },
            title = {
                Text(text = entity.name)
            },
            text = {
                Text(text = stringResource(Resources.string.tag_delete_confirmation_text))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        isConfirmationAlertVisible.value = false
                        deleteTag(entity)
                    }
                ) {
                    Text(text = stringResource(Resources.string.confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isConfirmationAlertVisible.value = false
                    }
                ) {
                    Text(text = stringResource(Resources.string.dismiss))
                }
            },
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
        modifier = modifier
            .height(IntrinsicSize.Max)
            .padding(horizontal = Large),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.weight(weight = 1f, fill = true),
        ) {
            DsSearchTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.searchQuery,
                onValueChanged = setSearchQuery,
            )
        }

        if (!state.isEditable) return

        DsButton(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = Normal),
            onClick = addTagClicked,
        ) {
            Text(text = stringResource(Resources.string.add))
        }
    }
}
