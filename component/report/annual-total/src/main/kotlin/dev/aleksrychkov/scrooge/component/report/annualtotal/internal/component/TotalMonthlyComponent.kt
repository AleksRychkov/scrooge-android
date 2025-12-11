package dev.aleksrychkov.scrooge.component.report.annualtotal.internal.component

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.component.udf.TotalMonthlyActor
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.component.udf.TotalMonthlyEvent
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.component.udf.TotalMonthlyReducer
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.component.udf.TotalMonthlyState
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import kotlinx.coroutines.flow.StateFlow

internal interface TotalMonthlyComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext
        ): TotalMonthlyComponent = DefaultTotalMonthlyComponent(componentContext = componentContext)
    }

    val state: StateFlow<TotalMonthlyState>
    fun setYear(year: Int)
}

private class DefaultTotalMonthlyComponent(
    componentContext: ComponentContext,
) : TotalMonthlyComponent, ComponentContext by componentContext {
    private val store: Store<TotalMonthlyState, TotalMonthlyEvent, Unit> by lazy {
        instanceKeeper.createStore(
            initialState = TotalMonthlyState(),
            actor = TotalMonthlyActor(),
            reducer = TotalMonthlyReducer(),
        )
    }

    override val state: StateFlow<TotalMonthlyState>
        get() = store.state

    override fun setYear(year: Int) {
        store.handle(TotalMonthlyEvent.External.Load(year = year))
    }
}
