package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf

import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersSettings
import kotlinx.collections.immutable.ImmutableSet
import java.util.EnumSet

internal sealed interface FiltersEvent {
    sealed interface External : FiltersEvent {
        data class Init(val filter: FilterEntity, val settings: EnumSet<FiltersSettings>) : External
        data class YearClicked(val year: Int) : External
        data class MonthClicked(val month: Int) : External
        data class ToggleTag(val tag: TagEntity) : External
    }

    sealed interface Internal : FiltersEvent {
        data class SetMinMaxYearsPeriod(val startYear: Int, val endYear: Int) : Internal
        data class AvailableTags(val tags: ImmutableSet<TagEntity>) : Internal
    }
}
