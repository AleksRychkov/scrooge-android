package dev.aleksrychkov.scrooge.presentation.component.tags

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large2X
import dev.aleksrychkov.scrooge.core.designsystem.theme.ListItemHeight
import dev.aleksrychkov.scrooge.core.designsystem.theme.Medium
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.presentation.component.tags.composable.TagsBar
import dev.aleksrychkov.scrooge.presentation.component.tags.internal.TagComponentInternal
import dev.aleksrychkov.scrooge.presentation.component.tags.internal.udf.TagEffect
import dev.aleksrychkov.scrooge.presentation.component.tags.internal.udf.TagState
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun TagSelectionContent(
    modifier: Modifier,
    component: TagComponent,
    initialSelection: ImmutableSet<TagEntity>,
    callback: (Set<TagEntity>) -> Unit,
) {
    TagSelectionContent(
        modifier = modifier,
        component = component as TagComponentInternal,
        initialSelection = initialSelection,
        callback = callback,
    )
}

@Composable
private fun TagSelectionContent(
    modifier: Modifier,
    component: TagComponentInternal,
    initialSelection: ImmutableSet<TagEntity>,
    callback: (Set<TagEntity>) -> Unit,
) {
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
    val contentListState = rememberLazyListState()
    Scaffold(
        modifier = modifier,
        topBar = {
            TagsBar(
                modifier = Modifier.fillMaxWidth(),
                component = component,
                contentListState = contentListState,
            )
        }
    ) { innerPadding ->
        TagContent(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize(),
            component = component,
            contentListState = contentListState,
            initialSelection = initialSelection,
            callback = callback,
        )
    }
}

@Composable
private fun TagContent(
    modifier: Modifier,
    component: TagComponentInternal,
    contentListState: LazyListState,
    initialSelection: ImmutableSet<TagEntity>,
    callback: (ImmutableSet<TagEntity>) -> Unit,
) {
    val state by component.state.collectAsStateWithLifecycle()

    val selectedTags = remember {
        SnapshotStateList<TagEntity>().also { list ->
            initialSelection.forEach(list::add)
        }
    }
    LaunchedEffect(component) {
        snapshotFlow { selectedTags.toList() }
            .distinctUntilChanged()
            .onEach { callback(it.toImmutableSet()) }
            .flowOn(Dispatchers.Default)
            .launchIn(this)
    }

    TagsList(
        modifier = modifier,
        state = state,
        selectedTags = selectedTags,
        contentListState = contentListState,
        deleteTag = component::deleteTag,
        selectTag = { tag ->
            if (!selectedTags.contains(tag)) {
                selectedTags.add(tag)
            }
        },
        unselectTag = { tag ->
            if (selectedTags.contains(tag)) {
                selectedTags.remove(tag)
            }
        }
    )
}

@Composable
private fun TagsList(
    modifier: Modifier,
    state: TagState,
    selectedTags: List<TagEntity>,
    contentListState: LazyListState,
    selectTag: (TagEntity) -> Unit,
    unselectTag: (TagEntity) -> Unit,
    deleteTag: (TagEntity) -> Unit,
) {
    Box(
        modifier = modifier.padding(bottom = Normal)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            state = contentListState,
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
                    isChecked = selectedTags.contains(tag),
                    isEditable = state.isEditable,
                    deleteTag = deleteTag,
                    selectTag = selectTag,
                    unselectTag = unselectTag,
                )
            }

            item {
                Spacer(Modifier.height(Large2X))
            }
        }

        val density = LocalDensity.current
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Large2X)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.background,
                        ),
                        endY = with(density) { Large2X.toPx() },
                    ),
                ),
        )
    }
}

@Composable
private fun Tag(
    modifier: Modifier = Modifier,
    entity: TagEntity,
    isChecked: Boolean,
    isEditable: Boolean,
    deleteTag: (TagEntity) -> Unit,
    selectTag: (TagEntity) -> Unit,
    unselectTag: (TagEntity) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = ListItemHeight)
            .clickable {
                if (isChecked) unselectTag(entity) else selectTag(entity)
            }
            .padding(start = Large),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            modifier = Modifier
                .height(ListItemHeight)
                .padding(vertical = Normal)
                .padding(end = Normal)
                .aspectRatio(1f),
            checked = isChecked,
            onCheckedChange = {
                if (isChecked) unselectTag(entity) else selectTag(entity)
            },
        )

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
                        unselectTag(entity)
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
