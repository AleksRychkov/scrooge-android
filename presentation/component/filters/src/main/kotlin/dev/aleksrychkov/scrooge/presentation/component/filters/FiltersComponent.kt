package dev.aleksrychkov.scrooge.presentation.component.filters

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.DefaultFiltersComponent

interface FiltersComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext
        ): FiltersComponent = DefaultFiltersComponent(
            componentContext = componentContext,
        )
    }
}
