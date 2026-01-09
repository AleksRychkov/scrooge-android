package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersSettings
import java.util.EnumSet

internal sealed interface FiltersEvent {
    sealed interface External : FiltersEvent {
        data class Init(val filter: FilterEntity, val settings: EnumSet<FiltersSettings>) : External
        data class YearClicked(val year: Int, val isLongClick: Boolean = false) : External
        data class MonthClicked(val month: Int, val isLongClick: Boolean = false) : External
        data class RemoveTag(val tag: TagEntity) : External
        data class AddTag(val tag: TagEntity) : External
        data class SetCategory(val category: CategoryEntity) : External
        data object RemoveCategory : External
        data object Reset : External
    }

    sealed interface Internal : FiltersEvent {
        data class SetMinMaxYearsPeriod(val startYear: Int, val endYear: Int) : Internal
    }
}
