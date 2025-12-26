package dev.aleksrychkov.scrooge.presentation.screen.transaction.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.entity.startEndOfMonth
import dev.aleksrychkov.scrooge.core.router.DestinationTransactionForm
import dev.aleksrychkov.scrooge.core.router.Router
import dev.aleksrychkov.scrooge.core.router.context.RouterComponentContext
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersComponent
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.PeriodTotalComponent
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.TransactionsListComponent
import dev.aleksrychkov.scrooge.presentation.screen.transaction.internal.component.period.PeriodComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.Instant

internal class DefaultTransactionsComponent(
    private val componentContext: ComponentContext
) : TransactionsComponentInternal, ComponentContext by componentContext {

    private val periodNavigation = SlotNavigation<Instant>()
    private val filtersNavigation = SlotNavigation<Unit>()

    private val router: Router by lazy {
        (componentContext as RouterComponentContext).router
    }

    private val _state = MutableStateFlow(TransactionsState())

    private val _filtersComponent: FiltersComponent by lazy {
        FiltersComponent(
            componentContext = childContext("TransactionsComponentFiltersChildContext")
        )
    }

    private val _periodTotalComponent: PeriodTotalComponent by lazy {
        PeriodTotalComponent(
            componentContext = childContext("TransactionsComponentPeriodTotalChildContext")
        ).also {
            val period = startEndOfMonth(_state.value.selectedPeriod)
            it.setPeriod(period = period)
        }
    }

    private val _transactionsListComponent: TransactionsListComponent by lazy {
        val instant = state.value.selectedPeriod
        val period = startEndOfMonth(instant)
        TransactionsListComponent(
            componentContext = childContext("TransactionsComponentTransactionsListChildContenxt"),
            period = period,
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

    override val filtersModal: Value<ChildSlot<*, FiltersComponent>> =
        childSlot(
            source = filtersNavigation,
            serializer = null,
            handleBackButton = true,
            key = "FiltersModalSlot",
        ) { _, childComponentContext ->
            FiltersComponent(
                componentContext = childComponentContext,
            )
        }

    override val state: StateFlow<TransactionsState>
        get() = _state.asStateFlow()

    override val periodTotalComponent: PeriodTotalComponent
        get() = _periodTotalComponent

    override val transactionsListComponent: TransactionsListComponent
        get() = _transactionsListComponent

    override val filtersComponent: FiltersComponent
        get() = _filtersComponent

    override fun addIncome() {
        DestinationTransactionForm.addIncome().let(router::open)
    }

    override fun addExpense() {
        DestinationTransactionForm.addExpense().let(router::open)
    }

    override fun openPeriodModal(instant: Instant) {
//        periodNavigation.activate(instant)
        filtersNavigation.activate(Unit)
    }

    override fun closePeriodModal() {
//        periodNavigation.dismiss()
        filtersNavigation.dismiss()
    }

    override fun setPeriod(instant: Instant) {
        _state.value = TransactionsState(instant)
        val period = startEndOfMonth(instant)
        _periodTotalComponent.setPeriod(period = period)
        _transactionsListComponent.setPeriod(period = period)
    }
}
