package dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.IncomeExpenseChartComponent
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.udf.IncomeExpenseChartActor
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.udf.IncomeExpenseChartEvent
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.udf.IncomeExpenseChartReducer
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.udf.IncomeExpenseChartState
import kotlinx.coroutines.flow.StateFlow

internal interface IncomeExpenseChartComponentInternal : IncomeExpenseChartComponent {
    val state: StateFlow<IncomeExpenseChartState>
    fun retry()
}

internal class DefaultIncomeExpenseChartComponent(
    componentContext: ComponentContext,
) : IncomeExpenseChartComponentInternal, ComponentContext by componentContext {
    private val store: Store<IncomeExpenseChartState, IncomeExpenseChartEvent, Unit> by lazy {
        instanceKeeper.createStore(
            initialState = IncomeExpenseChartState(),
            actor = IncomeExpenseChartActor(),
            reducer = IncomeExpenseChartReducer(),
        )
    }

    override val state: StateFlow<IncomeExpenseChartState>
        get() = store.state

    override fun setFilters(filter: FilterEntity) {
        store.handle(IncomeExpenseChartEvent.External.SetFilter(filter))
    }

    override fun retry() {
        store.handle(IncomeExpenseChartEvent.External.Retry)
    }
}
