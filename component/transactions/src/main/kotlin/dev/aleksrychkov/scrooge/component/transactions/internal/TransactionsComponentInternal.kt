package dev.aleksrychkov.scrooge.component.transactions.internal

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.transactions.TransactionsComponent
import dev.aleksrychkov.scrooge.component.transactions.internal.component.balance.BalanceComponent
import dev.aleksrychkov.scrooge.component.transactions.internal.component.period.PeriodComponent
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Instant

internal interface TransactionsComponentInternal : TransactionsComponent {
    val periodModal: Value<ChildSlot<*, PeriodComponent>>

    val state: StateFlow<TransactionsState>

    val balanceComponent: BalanceComponent

    fun addIncome()
    fun addExpense()

    fun openPeriodModal(instant: Instant)
    fun closePeriodModal()
    fun setPeriod(instant: Instant)
}
