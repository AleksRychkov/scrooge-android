package dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersComponent
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.PeriodTotalComponent
import dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.ReportAnnualTotalComponent
import dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.component.totalMonthly.TotalMonthlyComponent
import kotlinx.coroutines.flow.StateFlow

internal interface ReportAnnualTotalComponentInternal : ReportAnnualTotalComponent {
    val filtersModal: Value<ChildSlot<*, FiltersComponent>>

    val state: StateFlow<ReportAnnualTotalState>

    val periodTotalComponent: PeriodTotalComponent
    val totalMonthlyComponent: TotalMonthlyComponent

    fun openFiltersModal()
    fun closeFiltersModal()
    fun setFilter(filter: FilterEntity)

    fun openCategoryTotal(period: PeriodTimestampEntity)
}
