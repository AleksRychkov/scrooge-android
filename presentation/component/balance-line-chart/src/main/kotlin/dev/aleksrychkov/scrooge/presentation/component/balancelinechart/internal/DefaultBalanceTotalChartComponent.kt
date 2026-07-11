package dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.BalanceTotalChartComponent
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.udf.BalanceLineChartEvent
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.udf.BalanceLineChartReducer
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.udf.BalanceLineChartState
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.udf.BalanceTotalChartActor
import kotlinx.coroutines.flow.StateFlow

internal interface BalanceTotalChartComponentInternal : BalanceTotalChartComponent {
    val state: StateFlow<BalanceLineChartState>
    fun retry()
}

internal class DefaultBalanceTotalChartComponent(
    componentContext: ComponentContext,
) : BalanceTotalChartComponentInternal, ComponentContext by componentContext {
    private val store: Store<BalanceLineChartState, BalanceLineChartEvent, Unit> by lazy {
        instanceKeeper.createStore(
            initialState = BalanceLineChartState(),
            actor = BalanceTotalChartActor(),
            reducer = BalanceLineChartReducer(),
        )
    }

    override val state: StateFlow<BalanceLineChartState>
        get() = store.state

    override fun setFilters(filter: FilterEntity) {
        store.handle(BalanceLineChartEvent.External.SetFilter(filter))
    }

    override fun retry() {
        store.handle(BalanceLineChartEvent.External.Retry)
    }
}
