package dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsSecondaryCard
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.CategoryCarouselComponent
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.CategoryCarouselContent

@Composable
internal fun FormCategory(
    modifier: Modifier,
    component: CategoryCarouselComponent,
    category: CategoryEntity?,
) {
    LaunchedEffect(category) {
        if (category != null) component.setCategory(category)
    }
    DsSecondaryCard(modifier = modifier) {
        CategoryCarouselContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Normal),
            component = component,
        )
    }
}
