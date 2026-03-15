package dev.aleksrychkov.scrooge.presentation.component.categorycarousel

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.CategoryCarouselComponentInternal

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
@Suppress("UnusedParameter")
private fun CategoryCarouselContent(
    modifier: Modifier,
    component: CategoryCarouselComponentInternal,
) {
    Column(
        modifier = modifier
    ) {
        Text("Carousel")
    }
}
