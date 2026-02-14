package dev.aleksrychkov.scrooge.presentation.screen.hub.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.router.DestinationTransactionForm
import dev.aleksrychkov.scrooge.core.router.Router
import dev.aleksrychkov.scrooge.core.router.context.RouterComponentContext
import dev.aleksrychkov.scrooge.core.udfextensions.retainedCoroutineScope
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersComponent
import dev.aleksrychkov.scrooge.presentation.component.limits.LimitsComponent
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.PeriodTotalComponent
import dev.aleksrychkov.scrooge.presentation.component.transactionlist.TransactionsListComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class DefaultHubComponent(
    private val componentContext: ComponentContext,
) : HubComponentInternal, ComponentContext by componentContext {
    private val filtersNavigation = SlotNavigation<FilterEntity>()

    private val router: Router by lazy {
        (componentContext as RouterComponentContext).router
    }

    private val _periodTotalComponent: PeriodTotalComponent by lazy {
        PeriodTotalComponent(
            componentContext = childContext("TransactionsComponentPeriodTotalChildContext")
        )
    }

    private val _transactionsListComponent: TransactionsListComponent by lazy {
        TransactionsListComponent(
            componentContext = childContext("TransactionsComponentTransactionsListChildContext"),
        )
    }

    private val _limitsComponent: LimitsComponent by lazy {
        LimitsComponent(
            componentContext = childContext("TransactionsComponentLimitsChildContext"),
        )
    }

    private val _state = MutableStateFlow(HubState())

    init {
        retainedCoroutineScope(dispatcher = Dispatchers.IO).launch {
            val initialFilters = FilterEntity.currentMonth()
            _state.value = _state.value.copy(filter = initialFilters)
            _periodTotalComponent.setFilters(initialFilters)
            _transactionsListComponent.setFilters(initialFilters)
        }
    }

    override val state: StateFlow<HubState>
        get() = _state.asStateFlow()

    override val filtersModal: Value<ChildSlot<*, FiltersComponent>> =
        childSlot(
            source = filtersNavigation,
            serializer = null,
            handleBackButton = true,
            key = "FiltersModalSlot",
        ) { filter, childComponentContext ->
            FiltersComponent(
                componentContext = childComponentContext,
                filter = filter,
            )
        }

    override val periodTotalComponent: PeriodTotalComponent
        get() = _periodTotalComponent

    override val transactionsListComponent: TransactionsListComponent
        get() = _transactionsListComponent

    override val limitsComponent: LimitsComponent
        get() = _limitsComponent

    override fun addIncome() {
        DestinationTransactionForm.addIncome().let(router::open)
    }

    override fun addExpense() {
        DestinationTransactionForm.addExpense().let(router::open)
    }

    override fun openFiltersModal() {
        filtersNavigation.activate(_state.value.filter)
    }

    override fun closeFiltersModal() {
        filtersNavigation.dismiss()
    }

    override fun setFilter(filter: FilterEntity) {
        _state.value = _state.value.copy(filter = filter)
        _periodTotalComponent.setFilters(filter)
        _transactionsListComponent.setFilters(filter)
    }
}
