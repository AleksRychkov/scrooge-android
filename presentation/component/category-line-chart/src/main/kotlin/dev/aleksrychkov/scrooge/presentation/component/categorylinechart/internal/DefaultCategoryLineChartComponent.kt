package dev.aleksrychkov.scrooge.presentation.component.categorylinechart.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import dev.aleksrychkov.scrooge.presentation.component.categorylinechart.CategoryLineChartComponent
import dev.aleksrychkov.scrooge.presentation.component.categorylinechart.internal.udf.CategoryLineChartActor
import dev.aleksrychkov.scrooge.presentation.component.categorylinechart.internal.udf.CategoryLineChartEvent
import dev.aleksrychkov.scrooge.presentation.component.categorylinechart.internal.udf.CategoryLineChartReducer
import dev.aleksrychkov.scrooge.presentation.component.categorylinechart.internal.udf.CategoryLineChartState
import kotlinx.coroutines.flow.StateFlow

internal interface CategoryLineChartComponentInternal : CategoryLineChartComponent {
    val state: StateFlow<CategoryLineChartState>
    fun retry()
}

internal class DefaultCategoryLineChartComponent(
    componentContext: ComponentContext,
) : CategoryLineChartComponentInternal, ComponentContext by componentContext {
    private val store: Store<CategoryLineChartState, CategoryLineChartEvent, Unit> by lazy {
        instanceKeeper.createStore(
            initialState = CategoryLineChartState(),
            actor = CategoryLineChartActor(),
            reducer = CategoryLineChartReducer(),
        )
    }

    override val state: StateFlow<CategoryLineChartState>
        get() = store.state

    override fun setFilters(filter: FilterEntity) {
        store.handle(CategoryLineChartEvent.External.SetFilter(filter))
    }

    override fun retry() {
        store.handle(CategoryLineChartEvent.External.Retry)
    }
}
