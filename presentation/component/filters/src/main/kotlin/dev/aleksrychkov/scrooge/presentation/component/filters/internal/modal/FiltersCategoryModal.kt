package dev.aleksrychkov.scrooge.presentation.component.filters.internal.modal

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import dev.aleksrychkov.scrooge.presentation.component.category.CategoryModal
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.FiltersComponentInternal

@Composable
internal fun FiltersCategoryModal(
    component: FiltersComponentInternal,
) {
    val slot = component.categoryModal.subscribeAsState().value
    CategoryModal(
        slot = slot,
        close = component::closeCategoryModal,
        select = component::setCategory,
    )
}
