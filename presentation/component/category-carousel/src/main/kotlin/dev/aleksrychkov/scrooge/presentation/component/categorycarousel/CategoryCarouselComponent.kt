package dev.aleksrychkov.scrooge.presentation.component.categorycarousel

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.presentation.component.category.CategoryComponent
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.CategoryCarouselComponentInternalStub
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.DefaultCategoryCarouselComponent
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.udf.CategoryCarouselState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface CategoryCarouselComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
            type: TransactionType,
            callback: (CategoryEntity) -> Unit,
        ): CategoryCarouselComponent {
            return DefaultCategoryCarouselComponent(
                componentContext = componentContext,
                type = type,
                callback = callback,
            )
        }

        fun stub(): CategoryCarouselComponent = object : CategoryCarouselComponentInternalStub {
            override val categoryModal: Value<ChildSlot<*, CategoryComponent>>
                get() = MutableValue(ChildSlot<Any, CategoryComponent>())

            override val state: StateFlow<CategoryCarouselState>
                get() = MutableStateFlow(CategoryCarouselState())
        }
    }

    fun setCategory(category: CategoryEntity) {}
}
