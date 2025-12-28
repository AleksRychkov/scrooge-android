package dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.component.totalMonthly

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.component.totalMonthly.udf.TotalMonthlyActor
import dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.component.totalMonthly.udf.TotalMonthlyEvent
import dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.component.totalMonthly.udf.TotalMonthlyReducer
import dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.component.totalMonthly.udf.TotalMonthlyState
import kotlinx.coroutines.flow.StateFlow

internal interface TotalMonthlyComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext
        ): TotalMonthlyComponent = DefaultTotalMonthlyComponent(componentContext = componentContext)
    }

    val state: StateFlow<TotalMonthlyState>
    fun setFilters(filter: FilterEntity)
}

private class DefaultTotalMonthlyComponent(
    componentContext: ComponentContext,
) : TotalMonthlyComponent, ComponentContext by componentContext {
    private val store: Store<TotalMonthlyState, TotalMonthlyEvent, Unit> by lazy {
        instanceKeeper.createStore(
            initialState = TotalMonthlyState(),
            actor = TotalMonthlyActor.Companion(),
            reducer = TotalMonthlyReducer(),
        )
    }

    override val state: StateFlow<TotalMonthlyState>
        get() = store.state

    override fun setFilters(filter: FilterEntity) {
        store.handle(TotalMonthlyEvent.External.Load(filter = filter))
    }
}
