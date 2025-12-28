package dev.aleksrychkov.scrooge.presentation.component.filters

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.DefaultFiltersComponent
import java.util.EnumSet

interface FiltersComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
            filter: FilterEntity,
            settings: EnumSet<FiltersSettings> = EnumSet.allOf(FiltersSettings::class.java),
        ): FiltersComponent = DefaultFiltersComponent(
            componentContext = componentContext,
            filter = filter,
            settings = settings,
        )
    }
}
