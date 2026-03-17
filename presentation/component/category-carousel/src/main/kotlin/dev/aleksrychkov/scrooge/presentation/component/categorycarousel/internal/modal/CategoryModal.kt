package dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.modal

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.slot.ChildSlot
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.presentation.component.category.CategoryComponent
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.CategoryCarouselComponentInternal

@Composable
internal fun CategoryModal(
    component: CategoryCarouselComponentInternal
) {
    val slot = component.categoryModal.subscribeAsState().value

    Modal(
        slot = slot,
        close = component::closeCategoryModal,
        select = component::setCategory,
    )
}

@Composable
private fun Modal(
    slot: ChildSlot<*, CategoryComponent>,
    close: () -> Unit,
    select: (CategoryEntity) -> Unit,
) {
    dev.aleksrychkov.scrooge.presentation.component.category.CategoryModal(
        slot = slot,
        close = close,
        select = select,
    )
}
