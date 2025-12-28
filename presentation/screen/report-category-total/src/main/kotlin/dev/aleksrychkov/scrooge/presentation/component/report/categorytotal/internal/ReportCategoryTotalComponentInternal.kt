package dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersComponent
import dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.ReportCategoryTotalComponent
import dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal.component.bycategory.ByCategoryComponent
import kotlinx.coroutines.flow.StateFlow

internal interface ReportCategoryTotalComponentInternal : ReportCategoryTotalComponent {
    val filtersModal: Value<ChildSlot<*, FiltersComponent>>

    val state: StateFlow<ReportCategoryState>

    val byCategoryComponent: ByCategoryComponent

    fun openFiltersModal()
    fun closeFiltersModal()
    fun setFilter(filter: FilterEntity)

    fun onBackClicked()
}
