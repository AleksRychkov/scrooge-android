package dev.aleksrychkov.scrooge.presentation.screen.transaction.internal

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.report.periodtotal.PeriodTotalComponent
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.TransactionsListComponent
import dev.aleksrychkov.scrooge.presentation.screen.transaction.TransactionsComponent
import dev.aleksrychkov.scrooge.presentation.screen.transaction.internal.component.period.PeriodComponent
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Instant

internal interface TransactionsComponentInternal : TransactionsComponent {
    val periodModal: Value<ChildSlot<*, PeriodComponent>>

    val state: StateFlow<TransactionsState>

    val periodTotalComponent: PeriodTotalComponent
    val transactionsListComponent: TransactionsListComponent

    fun addIncome()
    fun addExpense()

    fun openPeriodModal(instant: Instant)
    fun closePeriodModal()
    fun setPeriod(instant: Instant)
}
