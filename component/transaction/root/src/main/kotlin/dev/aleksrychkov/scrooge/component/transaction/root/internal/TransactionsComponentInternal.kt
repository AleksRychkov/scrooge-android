package dev.aleksrychkov.scrooge.component.transaction.root.internal

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.transaction.list.TransactionsListComponent
import dev.aleksrychkov.scrooge.component.transaction.root.TransactionsComponent
import dev.aleksrychkov.scrooge.component.transaction.root.internal.component.balance.BalanceComponent
import dev.aleksrychkov.scrooge.component.transaction.root.internal.component.period.PeriodComponent
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Instant

internal interface TransactionsComponentInternal : TransactionsComponent {
    val periodModal: Value<ChildSlot<*, PeriodComponent>>

    val state: StateFlow<TransactionsState>

    val balanceComponent: BalanceComponent
    val transactionsListComponent: TransactionsListComponent

    fun addIncome()
    fun addExpense()

    fun openPeriodModal(instant: Instant)
    fun closePeriodModal()
    fun setPeriod(instant: Instant)
}
