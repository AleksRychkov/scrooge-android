@file:Suppress("All")

package dev.aleksrychkov.scrooge.presentation.component.filters

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.FiltersComponentInternal

@Composable
fun FiltersContent(
    modifier: Modifier,
    component: FiltersComponent,
    callback: (FilterEntity) -> Unit,
) {
    FiltersContent(
        modifier = modifier,
        component = component as FiltersComponentInternal,
    )
}

@Composable
private fun FiltersContent(
    modifier: Modifier,
    component: FiltersComponentInternal,
) {
}
