package dev.aleksrychkov.scrooge.component.transactions.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.transactions.internal.component.balance.BalanceComponent
import dev.aleksrychkov.scrooge.component.transactions.internal.component.period.PeriodComponent
import dev.aleksrychkov.scrooge.component.transactions.internal.utils.DateTimeUtils
import dev.aleksrychkov.scrooge.component.transactionslist.TransactionsListComponent
import dev.aleksrychkov.scrooge.core.router.DestinationTransactionForm
import dev.aleksrychkov.scrooge.core.router.Router
import dev.aleksrychkov.scrooge.core.router.context.RouterComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.Instant

internal class DefaultTransactionsComponent(
    private val componentContext: ComponentContext
) : TransactionsComponentInternal, ComponentContext by componentContext {

    private val periodNavigation = SlotNavigation<Instant>()

    private val router: Router by lazy {
        (componentContext as RouterComponentContext).router
    }

    private val _state = MutableStateFlow(TransactionsState())

    private val _balanceComponent: BalanceComponent by lazy {
        BalanceComponent(
            componentContext = childContext("BalanceComponentContext")
        ).also {
            it.setPeriod(_state.value.selectedPeriod)
        }
    }

    private val _transactionsListComponent: TransactionsListComponent by lazy {
        val period = state.value.selectedPeriod
        val startEnd = DateTimeUtils.getMonthStartEndTimestamp(period)
        TransactionsListComponent(
            componentContext = childContext("TransactionsListComponent"),
            period = startEnd,
        )
    }

    override val periodModal: Value<ChildSlot<*, PeriodComponent>> =
        childSlot(
            source = periodNavigation,
            serializer = null,
            handleBackButton = true,
            key = "PeriodModalSlot",
        ) { instant, childComponentContext ->
            PeriodComponent(
                componentContext = childComponentContext,
                instant = instant,
            )
        }

    override val state: StateFlow<TransactionsState>
        get() = _state.asStateFlow()

    override val balanceComponent: BalanceComponent
        get() = _balanceComponent

    override val transactionsListComponent: TransactionsListComponent
        get() = _transactionsListComponent

    override fun addIncome() {
        DestinationTransactionForm.addIncome().let(router::open)
    }

    override fun addExpense() {
        DestinationTransactionForm.addExpense().let(router::open)
    }

    override fun openPeriodModal(instant: Instant) {
        periodNavigation.activate(instant)
    }

    override fun closePeriodModal() {
        periodNavigation.dismiss()
    }

    override fun setPeriod(instant: Instant) {
        _state.value = TransactionsState(instant)
        _balanceComponent.setPeriod(instant = instant)
        val startEnd = DateTimeUtils.getMonthStartEndTimestamp(instant)
        _transactionsListComponent.setPeriod(period = startEnd)
    }
}
