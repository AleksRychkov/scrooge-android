package dev.aleksrychkov.scrooge.presentation.component.categorycarousel

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Medium
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Small
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.CategoryCarouselComponentInternal
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.modal.CategoryModal
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.udf.CarouselItem
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.udf.CategoryCarouselState
import kotlinx.collections.immutable.ImmutableList
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun CategoryCarouselContent(
    modifier: Modifier,
    component: CategoryCarouselComponent,
) {
    CategoryCarouselContent(
        modifier = modifier,
        component = component as CategoryCarouselComponentInternal,
    )
}

@Composable
private fun CategoryCarouselContent(
    modifier: Modifier,
    component: CategoryCarouselComponentInternal,
) {
    val state by component.state.collectAsStateWithLifecycle()
    CategoryCarouselContent(
        modifier = modifier,
        state = state,
        onItemClicked = component::selectItem,
        onCategoryListClicked = component::openCategoryModal,
    )

    CategoryModal(
        component = component,
    )
}

@Composable
private fun CategoryCarouselContent(
    modifier: Modifier,
    state: CategoryCarouselState,
    onItemClicked: (CarouselItem) -> Unit,
    onCategoryListClicked: () -> Unit,
) {
    val itemSize = 56.dp

    Column(
        modifier = modifier
    ) {
        CarouselContent(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemSize),
            list = state.carousel,
            itemSize = itemSize,
            selected = state.selectedCategory,
            onItemClicked = onItemClicked,
            onCategoryListClicked = onCategoryListClicked,
        )

        Spacer(modifier = Modifier.height(Medium))

        SelectedCategory(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Normal),
            category = state.selectedCategory,
        )
    }
}

@Composable
private fun SelectedCategory(
    modifier: Modifier,
    category: CategoryEntity?,
) {
    val (txt, color) = if (category == null) {
        stringResource(Resources.string.undefined) to MaterialTheme.colorScheme.onBackground.copy(
            alpha = 0.25f
        )
    } else {
        category.name to MaterialTheme.colorScheme.primary
    }

    AnimatedContent(
        modifier = modifier,
        targetState = txt,
        transitionSpec = {
            slideInVertically { -it } togetherWith slideOutVertically { it }
        }
    ) { txt ->
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = txt,
            color = color,
            textAlign = TextAlign.Center,
            softWrap = false,
        )
    }
}

@Composable
private fun CarouselContent(
    modifier: Modifier,
    list: ImmutableList<CarouselItem>,
    itemSize: Dp,
    selected: CategoryEntity?,
    onItemClicked: (CarouselItem) -> Unit,
    onCategoryListClicked: () -> Unit,
) {
    val state = rememberLazyListState()
    LazyRow(
        modifier = modifier,
        horizontalArrangement = spacedBy(Small),
        state = state,
        contentPadding = PaddingValues(horizontal = Normal),
    ) {
        item {
            Box(
                modifier = Modifier
                    .size(itemSize)
                    .clip(RoundedCornerShape(Normal))
                    .debounceClickable(onClick = onCategoryListClicked)
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.15f))
                        .padding(Normal),
                    tint = MaterialTheme.colorScheme.onBackground,
                    imageVector = Icons.AutoMirrored.Filled.FormatListBulleted,
                    contentDescription = null,
                )
            }
        }

        items(
            items = list,
            key = { item -> item.id }
        ) { item ->
            Box(
                modifier = Modifier
                    .size(itemSize)
                    .clip(RoundedCornerShape(Normal))
                    .carouselItemBorder(isSelected = item.id == selected?.id)
                    .debounceClickable(onClick = {
                        onItemClicked(item)
                    })
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(item.color.copy(alpha = 0.15f))
                        .padding(Normal),
                    tint = item.tint,
                    imageVector = item.imageVector,
                    contentDescription = null,
                )
            }
        }
    }
    LaunchedEffect(selected) {
        if (selected != null) {
            state.animateScrollToItem(list.indexOfFirst { it.id == selected.id })
        }
    }
}

@Composable
private fun Modifier.carouselItemBorder(
    isSelected: Boolean
): Modifier {
    return this then if (isSelected) {
        Modifier.border(
            width = 2.dp,
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(Normal),
        )
    } else {
        Modifier
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun CategoryCarouselContentPreview() {
    AppTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            CategoryCarouselContent(
                modifier = Modifier.fillMaxWidth(),
                state = CategoryCarouselState(),
                onItemClicked = { _ -> },
                onCategoryListClicked = {},
            )
        }
    }
}
