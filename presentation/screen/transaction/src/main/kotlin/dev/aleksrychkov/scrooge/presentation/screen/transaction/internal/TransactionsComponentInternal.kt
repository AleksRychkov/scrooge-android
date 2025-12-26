package dev.aleksrychkov.scrooge.presentation.screen.transaction.internal

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersComponent
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.PeriodTotalComponent
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.TransactionsListComponent
import dev.aleksrychkov.scrooge.presentation.screen.transaction.TransactionsComponent
import dev.aleksrychkov.scrooge.presentation.screen.transaction.internal.component.period.PeriodComponent
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Instant

internal interface TransactionsComponentInternal : TransactionsComponent {
    val periodModal: Value<ChildSlot<*, PeriodComponent>>
    val filtersModal: Value<ChildSlot<*, FiltersComponent>>

    val state: StateFlow<TransactionsState>

    val filtersComponent: FiltersComponent
    val periodTotalComponent: PeriodTotalComponent
    val transactionsListComponent: TransactionsListComponent

    fun addIncome()
    fun addExpense()

    fun openPeriodModal(instant: Instant)
    fun closePeriodModal()
    fun setPeriod(instant: Instant)
}
