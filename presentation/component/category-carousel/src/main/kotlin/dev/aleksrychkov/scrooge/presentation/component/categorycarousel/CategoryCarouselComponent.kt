package dev.aleksrychkov.scrooge.presentation.component.categorycarousel

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.CategoryCarouselComponentInternalStub
import dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.DefaultCategoryCarouselComponent

interface CategoryCarouselComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext
        ): CategoryCarouselComponent {
            return DefaultCategoryCarouselComponent(componentContext = componentContext)
        }

        fun stub(): CategoryCarouselComponent = object : CategoryCarouselComponentInternalStub {}
    }
}
