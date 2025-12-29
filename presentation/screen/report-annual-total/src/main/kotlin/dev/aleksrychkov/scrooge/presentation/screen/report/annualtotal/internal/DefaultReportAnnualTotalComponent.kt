package dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.PeriodDatestampEntity
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.core.router.DestinationReportCategoryTotal
import dev.aleksrychkov.scrooge.core.router.Router
import dev.aleksrychkov.scrooge.core.router.context.RouterComponentContext
import dev.aleksrychkov.scrooge.core.udfextensions.retainedCoroutineScope
import dev.aleksrychkov.scrooge.presentation.component.filters.FilterEntityFactory
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersComponent
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersSettings
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.PeriodTotalComponent
import dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.component.totalMonthly.TotalMonthlyComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.EnumSet

internal class DefaultReportAnnualTotalComponent(
    componentContext: ComponentContext,
    private val resourceManager: Lazy<ResourceManager> = getLazy(),
) : ReportAnnualTotalComponentInternal, ComponentContext by componentContext {

    private val filtersNavigation = SlotNavigation<FilterEntity>()

    private val _state = MutableStateFlow(ReportAnnualTotalState())

    private val router: Router by lazy {
        (componentContext as RouterComponentContext).router
    }

    private val _periodTotalComponent: PeriodTotalComponent by lazy {
        PeriodTotalComponent(
            componentContext = childContext("ReportAnnualPeriodTotalComponentContext")
        )
    }

    private val _totalMonthlyComponent: TotalMonthlyComponent by lazy {
        TotalMonthlyComponent(
            componentContext = childContext("ReportAnnualTotalMonthlyComponentContext")
        )
    }

    init {
        retainedCoroutineScope(dispatcher = Dispatchers.IO).launch {
            val initialFilters = FilterEntityFactory.currentYear(resourceManager.value)
            _state.value = _state.value.copy(
                filtersName = initialFilters.readableName,
                filter = initialFilters
            )
            _periodTotalComponent.setFilters(initialFilters)
            _totalMonthlyComponent.setFilters(initialFilters)
        }
    }

    override val state: StateFlow<ReportAnnualTotalState>
        get() = _state.asStateFlow()

    override val periodTotalComponent: PeriodTotalComponent
        get() = _periodTotalComponent

    override val totalMonthlyComponent: TotalMonthlyComponent
        get() = _totalMonthlyComponent

    override val filtersModal: Value<ChildSlot<*, FiltersComponent>> =
        childSlot(
            source = filtersNavigation,
            serializer = null,
            handleBackButton = true,
            key = "ReportAnnualTotalFiltersModalSlot",
        ) { filter, childComponentContext ->
            FiltersComponent(
                componentContext = childComponentContext,
                filter = filter,
                settings = EnumSet.of(FiltersSettings.Years),
            )
        }

    override fun openFiltersModal() {
        filtersNavigation.activate(_state.value.filter)
    }

    override fun closeFiltersModal() {
        filtersNavigation.dismiss()
    }

    override fun setFilter(filter: FilterEntity) {
        _periodTotalComponent.setFilters(filter = filter)
        _totalMonthlyComponent.setFilters(filter = filter)
        _state.value = _state.value.copy(
            filtersName = filter.readableName,
            filter = filter,
        )
    }

    override fun openCategoryTotal(period: PeriodDatestampEntity) {
        router.open(
            DestinationReportCategoryTotal(
                FilterEntityFactory.fromPeriod(
                    period = period,
                    resourceManager = resourceManager.value,
                    tags = state.value.filter.tags
                )
            )
        )
    }
}
