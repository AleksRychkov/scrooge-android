package dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal

import androidx.compose.runtime.Immutable
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.presentation.component.category.CategoryComponent
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.CategoryCarouselComponent
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.udf.CategoryCarouselState
import kotlinx.coroutines.flow.StateFlow

@Immutable
internal interface CategoryCarouselComponentInternal : CategoryCarouselComponent {
    val categoryModal: Value<ChildSlot<*, CategoryComponent>>
    val state: StateFlow<CategoryCarouselState>

    fun selectItem(item: CategoryEntity) {}

    fun openCategoryModal() {}
    fun closeCategoryModal() {}
}

internal interface CategoryCarouselComponentInternalStub : CategoryCarouselComponentInternal
