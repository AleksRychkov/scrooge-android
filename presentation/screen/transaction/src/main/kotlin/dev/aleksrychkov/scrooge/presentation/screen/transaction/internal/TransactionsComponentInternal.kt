package dev.aleksrychkov.scrooge.presentation.screen.transaction.internal

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersComponent
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.PeriodTotalComponent
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.TransactionsListComponent
import dev.aleksrychkov.scrooge.presentation.screen.transaction.TransactionsComponent
import kotlinx.coroutines.flow.StateFlow

internal interface TransactionsComponentInternal : TransactionsComponent {
    val filtersModal: Value<ChildSlot<*, FiltersComponent>>

    val state: StateFlow<TransactionsState>

    val periodTotalComponent: PeriodTotalComponent
    val transactionsListComponent: TransactionsListComponent

    fun addIncome()
    fun addExpense()

    fun openFiltersModal()
    fun closeFiltersModal()
    fun setFilter(filter: FilterEntity)
}
