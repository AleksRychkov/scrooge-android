package dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.BalanceTotalChartComponent
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.udf.BalanceTotalChartActor
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.udf.BalanceTotalChartEvent
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.udf.BalanceTotalChartReducer
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.udf.BalanceTotalChartState
import kotlinx.coroutines.flow.StateFlow

internal interface BalanceTotalChartComponentInternal : BalanceTotalChartComponent {
    val state: StateFlow<BalanceTotalChartState>
    fun retry()
}

internal class DefaultBalanceTotalChartComponent(
    componentContext: ComponentContext,
) : BalanceTotalChartComponentInternal, ComponentContext by componentContext {
    private val store: Store<BalanceTotalChartState, BalanceTotalChartEvent, Unit> by lazy {
        instanceKeeper.createStore(
            initialState = BalanceTotalChartState(),
            actor = BalanceTotalChartActor(),
            reducer = BalanceTotalChartReducer(),
        )
    }

    override val state: StateFlow<BalanceTotalChartState>
        get() = store.state

    override fun setFilters(filter: FilterEntity) {
        store.handle(BalanceTotalChartEvent.External.SetFilter(filter))
    }

    override fun retry() {
        store.handle(BalanceTotalChartEvent.External.Retry)
    }
}
