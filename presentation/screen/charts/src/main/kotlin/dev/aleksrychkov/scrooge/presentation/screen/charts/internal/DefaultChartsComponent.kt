package dev.aleksrychkov.scrooge.presentation.screen.charts.internal

import androidx.compose.runtime.Immutable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.udfextensions.retainedCoroutineScope
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.BalanceLineChartComponent
import dev.aleksrychkov.scrooge.presentation.component.categorylinechart.CategoryLineChartComponent
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersComponent
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersSettings
import dev.aleksrychkov.scrooge.presentation.screen.charts.ChartsComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.EnumSet

@Immutable
internal data class ChartsState(val filter: FilterEntity = FilterEntity.currentYear())

internal sealed interface ChartsCommand {
    data class ResolveFilter(val filter: FilterEntity) : ChartsCommand
}

internal interface ChartsComponentInternal : ChartsComponent {
    val state: StateFlow<ChartsState>
    val filtersModal: Value<ChildSlot<*, FiltersComponent>>
    val balanceChart: BalanceLineChartComponent
    val categoryChart: CategoryLineChartComponent
    fun openFilters()
    fun closeFilters()
    fun setFilter(filter: FilterEntity)
}

internal class DefaultChartsComponent(
    componentContext: ComponentContext,
) : ChartsComponentInternal, ComponentContext by componentContext {
    private val filtersNavigation = SlotNavigation<FilterEntity>()
    private val _state = MutableStateFlow(ChartsState())

    private val filterResolver = ChartsFilterResolver()
    private val scope = retainedCoroutineScope(Dispatchers.IO)
    private var resolveCurrencyJob: Job? = null

    private val _balanceChart by lazy {
        BalanceLineChartComponent(childContext("ChartsBalanceLineChart"))
    }
    private val _categoryChart by lazy {
        CategoryLineChartComponent(childContext("ChartsCategoryLineChart"))
    }

    init {
        setFilter(_state.value.filter)
    }

    override val state: StateFlow<ChartsState>
        get() = _state.asStateFlow()

    override val filtersModal: Value<ChildSlot<*, FiltersComponent>> = childSlot(
        source = filtersNavigation,
        serializer = null,
        handleBackButton = true,
        key = "ChartsFiltersModal",
    ) { filter, childComponentContext ->
        FiltersComponent(
            componentContext = childComponentContext,
            filter = filter,
            settings = EnumSet.of(
                FiltersSettings.Years,
                FiltersSettings.Currency,
                FiltersSettings.Category,
            ),
        )
    }

    override val balanceChart: BalanceLineChartComponent
        get() = _balanceChart

    override val categoryChart: CategoryLineChartComponent
        get() = _categoryChart

    override fun openFilters() {
        filtersNavigation.activate(_state.value.filter)
    }

    override fun closeFilters() {
        filtersNavigation.dismiss()
    }

    override fun setFilter(filter: FilterEntity) {
        resolveCurrencyJob?.cancel()
        if (filter.currency == null || filter.category == null) {
            _state.value = ChartsState(filter)
            execute(ChartsCommand.ResolveFilter(filter))
            return
        }
        applyFilter(filter)
    }

    private fun execute(command: ChartsCommand) {
        resolveCurrencyJob = scope.launch {
            when (command) {
                is ChartsCommand.ResolveFilter -> {
                    val resolvedFilter = filterResolver(command.filter)
                    if (resolvedFilter.currency != null && resolvedFilter.category != null) {
                        applyFilter(resolvedFilter)
                    }
                }
            }
        }
    }

    private fun applyFilter(filter: FilterEntity) {
        _state.value = ChartsState(filter)
        _balanceChart.setFilters(filter)
        _categoryChart.setFilters(filter)
    }
}
