package dev.aleksrychkov.scrooge.presentation.component.category

import android.view.HapticFeedbackConstants
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.DragHandle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsButton
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsSearchTextField
import dev.aleksrychkov.scrooge.core.designsystem.composables.NavigationBarSpacer
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.CategoryIconSize
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.ListItemHeight
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Small
import dev.aleksrychkov.scrooge.core.designsystem.utils.reallyPerformHapticFeedback
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.resources.CategoryIcons
import dev.aleksrychkov.scrooge.core.resources.UncategorizedIcon
import dev.aleksrychkov.scrooge.presentation.component.category.internal.CategoryComponentInternal
import dev.aleksrychkov.scrooge.presentation.component.category.internal.modal.CreateCategoryModal
import dev.aleksrychkov.scrooge.presentation.component.category.internal.udf.CategoryEffect
import dev.aleksrychkov.scrooge.presentation.component.category.internal.udf.CategoryState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.ReorderableLazyListState
import sh.calvin.reorderable.rememberReorderableLazyListState
import dev.aleksrychkov.scrooge.core.resources.R as Resources

private const val SWAP_ORDER_INDEX_DEBOUNCE_DURATION = 500L

@Composable
fun CategoryContent(
    modifier: Modifier,
    component: CategoryComponent,
    callback: (CategoryEntity?) -> Unit,
) {
    CategoryContent(
        modifier = modifier,
        component = component as CategoryComponentInternal,
        callback = callback,
    )
}

@Composable
private fun CategoryContent(
    modifier: Modifier,
    component: CategoryComponentInternal,
    callback: (CategoryEntity?) -> Unit,
) {
    val state by component.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    DisposableEffect(component) {
        val job = scope.launch {
            component.effects
                .onEach { effect ->
                    when (effect) {
                        is CategoryEffect.ShowInfoMessage -> {
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
        CategoryContent(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            state = state,
            selectCategory = callback,
            deleteCategory = component::deleteCategory,
            editCategory = component::editCategory,
            setSearchQuery = component::setSearchQuery,
            addNewCategoryClicked = component::openAddCategoryModal,
            swapOrder = component::swapOrder,
        )
    }
    CreateCategoryModal(
        component = component,
    )
}

@Composable
private fun CategoryContent(
    modifier: Modifier,
    state: CategoryState,
    selectCategory: (CategoryEntity) -> Unit,
    deleteCategory: (CategoryEntity) -> Unit,
    editCategory: (CategoryEntity) -> Unit,
    setSearchQuery: (String) -> Unit,
    addNewCategoryClicked: () -> Unit,
    swapOrder: (List<Pair<Long, Int>>) -> Unit,
) {
    Column(
        modifier = modifier
            .displayCutoutPadding()
            .statusBarsPadding()
    ) {
        CategoryBar(
            modifier = Modifier.fillMaxWidth(),
            state = state,
            setSearchQuery = setSearchQuery,
            addCategoryClicked = addNewCategoryClicked,
        )

        Spacer(modifier = Modifier.height(Normal))

        CategoryList(
            modifier = Modifier.fillMaxSize(),
            state = state,
            selectCategory = selectCategory,
            deleteCategory = deleteCategory,
            editCategory = editCategory,
            swapOrder = swapOrder,
        )
    }
}

@OptIn(FlowPreview::class)
@Composable
private fun CategoryList(
    modifier: Modifier,
    state: CategoryState,
    selectCategory: (CategoryEntity) -> Unit,
    deleteCategory: (CategoryEntity) -> Unit,
    editCategory: (CategoryEntity) -> Unit,
    swapOrder: (List<Pair<Long, Int>>) -> Unit,
) {
    val view = LocalView.current
    val lazyListState = rememberLazyListState()
    var reorderableList by remember(state.categoriesHash) {
        mutableStateOf(state.categories.toList())
    }
    val reorderableLazyColumnState = rememberReorderableLazyListState(lazyListState) { from, to ->
        reorderableList = reorderableList.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
        view.reallyPerformHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
    }
    LaunchedEffect(reorderableList) {
        delay(SWAP_ORDER_INDEX_DEBOUNCE_DURATION)
        reorderableList.mapIndexed { index, entity -> entity.id to index }.let(swapOrder)
    }
    Box(
        modifier = modifier.padding(bottom = Normal)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize(),
            state = lazyListState,
        ) {
            if (state.searchQuery.isNotBlank()) {
                ordinalList(
                    list = state.filtered,
                    selectCategory = selectCategory,
                    deleteCategory = deleteCategory,
                    editCategory = editCategory,
                )
            } else {
                reorderableList(
                    list = reorderableList,
                    reorderableLazyColumnState = reorderableLazyColumnState,
                    selectCategory = selectCategory,
                    deleteCategory = deleteCategory,
                    editCategory = editCategory,
                )
            }
            item {
                NavigationBarSpacer()
            }
        }
    }
}

private fun LazyListScope.reorderableList(
    list: List<CategoryEntity>,
    reorderableLazyColumnState: ReorderableLazyListState,
    selectCategory: (CategoryEntity) -> Unit,
    deleteCategory: (CategoryEntity) -> Unit,
    editCategory: (CategoryEntity) -> Unit,
) {
    items(
        list,
        key = { category -> category.id }
    ) { category ->
        ReorderableItem(reorderableLazyColumnState, category.id) {
            val interactionSource = remember { MutableInteractionSource() }
            Category(
                modifier = Modifier
                    .animateItem()
                    .padding(start = Small),
                entity = category,
                selectCategory = selectCategory,
                deleteCategory = deleteCategory,
                editCategory = editCategory,
            ) {
                IconButton(
                    modifier = Modifier
                        .draggableHandle(interactionSource = interactionSource)
                        .clearAndSetSemantics { },
                    onClick = {},
                ) {
                    Icon(Icons.Rounded.DragHandle, contentDescription = null)
                }
            }
        }
    }
}

private fun LazyListScope.ordinalList(
    list: ImmutableList<CategoryEntity>,
    selectCategory: (CategoryEntity) -> Unit,
    deleteCategory: (CategoryEntity) -> Unit,
    editCategory: (CategoryEntity) -> Unit,
) {
    items(
        items = list,
        key = { category -> category.id }
    ) { category ->
        Category(
            modifier = Modifier
                .animateItem()
                .padding(start = Large),
            entity = category,
            selectCategory = selectCategory,
            deleteCategory = deleteCategory,
            editCategory = editCategory,
        )
    }
}

@Composable
private fun Category(
    modifier: Modifier = Modifier,
    entity: CategoryEntity,
    selectCategory: (CategoryEntity) -> Unit,
    deleteCategory: (CategoryEntity) -> Unit,
    editCategory: (CategoryEntity) -> Unit,
    reorderableHandle: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = ListItemHeight)
            .debounceClickable {
                selectCategory(entity)
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        reorderableHandle?.invoke()

        Icon(
            modifier = Modifier
                .height(CategoryIconSize)
                .width(CategoryIconSize)
                .clip(CircleShape)
                .background(Color(entity.color))
                .padding(Normal),
            tint = Color.White,
            imageVector = CategoryIcons.find { it.id == entity.iconId }?.icon
                ?: UncategorizedIcon.icon,
            contentDescription = null,
        )

        Text(
            modifier = Modifier
                .weight(weight = 1f, fill = true)
                .padding(start = Normal),
            text = entity.name,
        )

        CategoryOptions(
            entity = entity,
            deleteCategory = deleteCategory,
            editCategory = editCategory,
        )
    }
}

@Composable
private fun CategoryOptions(
    modifier: Modifier = Modifier,
    entity: CategoryEntity,
    deleteCategory: (CategoryEntity) -> Unit,
    editCategory: (CategoryEntity) -> Unit,
) {
    var isConfirmationAlertVisible by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .height(ListItemHeight)
            .padding(Normal)
            .aspectRatio(1f)
            .clip(CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        var dropDownExpanded by remember { mutableStateOf(false) }

        IconButton(onClick = { dropDownExpanded = !dropDownExpanded }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(Resources.string.more_options),
            )
        }
        DropdownMenu(
            expanded = dropDownExpanded,
            onDismissRequest = { dropDownExpanded = false },
            shape = CardDefaults.shape,
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(Resources.string.edit)) },
                onClick = {
                    dropDownExpanded = false
                    editCategory(entity)
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(Resources.string.category_delete)) },
                onClick = {
                    dropDownExpanded = false
                    isConfirmationAlertVisible = true
                }
            )
        }
    }

    if (!isConfirmationAlertVisible) return
    AlertDialog(
        onDismissRequest = {
            isConfirmationAlertVisible = false
        },
        title = {
            Text(text = entity.name)
        },
        text = {
            Text(text = stringResource(Resources.string.category_delete_confirmation_text))
        },
        confirmButton = {
            TextButton(
                onClick = {
                    isConfirmationAlertVisible = false
                    deleteCategory(entity)
                }
            ) {
                Text(text = stringResource(Resources.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    isConfirmationAlertVisible = false
                }
            ) {
                Text(text = stringResource(Resources.string.dismiss))
            }
        },
    )
}

@Composable
private fun CategoryBar(
    modifier: Modifier,
    state: CategoryState,
    setSearchQuery: (String) -> Unit,
    addCategoryClicked: () -> Unit,
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

        DsButton(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = Normal),
            onClick = addCategoryClicked,
        ) {
            Text(text = stringResource(Resources.string.add))
        }
    }
}
