package dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.CategoryCarouselComponent
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.CategoryCarouselContent

@Composable
internal fun FormCategory(
    modifier: Modifier,
    component: CategoryCarouselComponent,
) {
    CategoryCarouselContent(
        modifier = modifier,
        component = component,
    )
}
