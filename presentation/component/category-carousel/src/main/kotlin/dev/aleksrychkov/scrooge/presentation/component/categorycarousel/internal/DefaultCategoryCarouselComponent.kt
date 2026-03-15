package dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal

import com.arkivanov.decompose.ComponentContext

internal class DefaultCategoryCarouselComponent(
    componentContext: ComponentContext,
) : CategoryCarouselComponentInternal, ComponentContext by componentContext
