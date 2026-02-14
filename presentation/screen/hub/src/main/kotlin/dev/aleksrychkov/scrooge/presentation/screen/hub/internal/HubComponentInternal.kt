package dev.aleksrychkov.scrooge.presentation.screen.hub.internal

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersComponent
import dev.aleksrychkov.scrooge.presentation.component.limits.LimitsComponent
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.PeriodTotalComponent
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.TransactionsListComponent
import dev.aleksrychkov.scrooge.presentation.screen.hub.HubComponent
import kotlinx.coroutines.flow.StateFlow

internal interface HubComponentInternal : HubComponent {
    val filtersModal: Value<ChildSlot<*, FiltersComponent>>

    val state: StateFlow<HubState>

    val periodTotalComponent: PeriodTotalComponent
    val transactionsListComponent: TransactionsListComponent
    val limitsComponent: LimitsComponent

    fun addIncome()
    fun addExpense()

    fun openFiltersModal()
    fun closeFiltersModal()
    fun setFilter(filter: FilterEntity)
}
