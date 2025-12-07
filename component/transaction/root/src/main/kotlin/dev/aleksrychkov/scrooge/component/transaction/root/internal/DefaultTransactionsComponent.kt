package dev.aleksrychkov.scrooge.component.transaction.root.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.report.periodtotal.PeriodTotalComponent
import dev.aleksrychkov.scrooge.component.transaction.list.TransactionsListComponent
import dev.aleksrychkov.scrooge.component.transaction.root.internal.component.period.PeriodComponent
import dev.aleksrychkov.scrooge.component.transaction.root.internal.utils.DateTimeUtils
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

    private val _periodTotalComponent: PeriodTotalComponent by lazy {
        PeriodTotalComponent(
            componentContext = childContext("PeriodTotalComponentContext")
        ).also {
            val startEnd = DateTimeUtils.getMonthStartEndTimestamp(_state.value.selectedPeriod)
            it.setPeriod(fromTimestamp = startEnd.first, toTimestamp = startEnd.second)
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
            PeriodComponent.Companion(
                componentContext = childComponentContext,
                instant = instant,
            )
        }

    override val state: StateFlow<TransactionsState>
        get() = _state.asStateFlow()

    override val periodTotalComponent: PeriodTotalComponent
        get() = _periodTotalComponent

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
        val startEnd = DateTimeUtils.getMonthStartEndTimestamp(instant)
        _periodTotalComponent.setPeriod(
            fromTimestamp = startEnd.first,
            toTimestamp = startEnd.second,
        )
        _transactionsListComponent.setPeriod(period = startEnd)
    }
}
