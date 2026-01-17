package dev.aleksrychkov.scrooge.presentation.component.category

import android.view.HapticFeedbackConstants
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsButton
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsSearchTextField
import dev.aleksrychkov.scrooge.core.designsystem.composables.animateElevation
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppBarShadow
import dev.aleksrychkov.scrooge.core.designsystem.theme.CategoryIconSize
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large2X
import dev.aleksrychkov.scrooge.core.designsystem.theme.ListItemHeight
import dev.aleksrychkov.scrooge.core.designsystem.theme.Medium
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
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

    val contentListState = rememberLazyListState()
    Scaffold(
        modifier = modifier,
        topBar = {
            CategoryBar(
                modifier = Modifier.fillMaxWidth(),
                component = component,
                contentListState = contentListState,
            )
        }
    ) { innerPadding ->
        CategoryContent(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize(),
            component = component,
            contentListState = contentListState,
            selectCategory = callback,
        )
    }
    CreateCategoryModal(
        component = component,
    )
}

@Composable
private fun CategoryContent(
    modifier: Modifier,
    component: CategoryComponentInternal,
    contentListState: LazyListState,
    selectCategory: (CategoryEntity) -> Unit,
) {
    val state by component.state.collectAsStateWithLifecycle()

    CategoryList(
        modifier = modifier,
        state = state,
        contentListState = contentListState,
        selectCategory = selectCategory,
        deleteCategory = component::deleteCategory,
        editCategory = component::editCategory,
        swapOrder = component::swapOrder,
    )
}

@OptIn(FlowPreview::class)
@Composable
private fun CategoryList(
    modifier: Modifier,
    state: CategoryState,
    contentListState: LazyListState,
    selectCategory: (CategoryEntity) -> Unit,
    deleteCategory: (CategoryEntity) -> Unit,
    editCategory: (CategoryEntity) -> Unit,
    swapOrder: (List<Pair<Long, Int>>) -> Unit,
) {
    val view = LocalView.current
    var reorderableList by remember(state.categoriesHash) {
        mutableStateOf(state.categories.toList())
    }
    val reorderableLazyColumnState =
        rememberReorderableLazyListState(contentListState) { from, to ->
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
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize(),
            state = contentListState,
        ) {
            if (!state.isEditable || state.searchQuery.isNotBlank()) {
                val list = if (state.searchQuery.isNotBlank()) {
                    state.filtered
                } else {
                    state.categories
                }
                ordinalList(
                    list = list,
                    isEditable = state.isEditable,
                    selectCategory = selectCategory,
                    deleteCategory = deleteCategory,
                    editCategory = editCategory,
                )
            } else {
                reorderableList(
                    list = reorderableList,
                    isEditable = true,
                    reorderableLazyColumnState = reorderableLazyColumnState,
                    selectCategory = selectCategory,
                    deleteCategory = deleteCategory,
                    editCategory = editCategory,
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
                )
        )
    }
}

private fun LazyListScope.reorderableList(
    list: List<CategoryEntity>,
    isEditable: Boolean,
    reorderableLazyColumnState: ReorderableLazyListState,
    selectCategory: (CategoryEntity) -> Unit,
    deleteCategory: (CategoryEntity) -> Unit,
    editCategory: (CategoryEntity) -> Unit,
) {
    items(
        list,
        key = { category -> "${category.id}-${category.type.type}" }
    ) { category ->
        ReorderableItem(reorderableLazyColumnState, "${category.id}-${category.type.type}") {
            val interactionSource = remember { MutableInteractionSource() }
            Category(
                modifier = Modifier.animateItem(),
                entity = category,
                isEditable = isEditable,
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
    isEditable: Boolean,
    selectCategory: (CategoryEntity) -> Unit,
    deleteCategory: (CategoryEntity) -> Unit,
    editCategory: (CategoryEntity) -> Unit,
) {
    items(
        items = list,
        key = { category -> "${category.id}-${category.type.type}" }
    ) { category ->
        Category(
            modifier = Modifier.animateItem(),
            entity = category,
            isEditable = isEditable,
            selectCategory = selectCategory,
            deleteCategory = deleteCategory,
            editCategory = editCategory,
        )
    }
}

@Composable
private fun Category(
    modifier: Modifier = Modifier,
    isEditable: Boolean,
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
            .debounceClickable { selectCategory(entity) }
            .padding(end = Medium),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        reorderableHandle?.invoke() ?: run {
            Spacer(modifier = Modifier.width(Large))
        }

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

        if (!isEditable) return
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
            .padding(vertical = Normal)
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
    component: CategoryComponentInternal,
    contentListState: LazyListState,
) {
    val state by component.state.collectAsStateWithLifecycle()

    val headerElevation by remember {
        derivedStateOf {
            if (contentListState.firstVisibleItemScrollOffset > 0 ||
                contentListState.firstVisibleItemIndex != 0
            ) {
                AppBarShadow
            } else {
                0.dp
            }
        }
    }
    val animatedElevation by headerElevation.animateElevation()

    Surface(
        Modifier.fillMaxWidth(),
        shadowElevation = animatedElevation,
    ) {
        Row(
            modifier = modifier
                .height(IntrinsicSize.Max)
                .padding(Large),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.weight(weight = 1f, fill = true),
            ) {
                DsSearchTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.searchQuery,
                    onValueChanged = component::setSearchQuery,
                )
            }

            if (state.isEditable) {
                DsButton(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = Normal),
                    onClick = component::openAddCategoryModal,
                ) {
                    Text(text = stringResource(Resources.string.add))
                }
            }
        }
    }
}
