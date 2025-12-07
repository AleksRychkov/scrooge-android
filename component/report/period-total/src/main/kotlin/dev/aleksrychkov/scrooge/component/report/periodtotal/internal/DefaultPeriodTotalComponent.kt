package dev.aleksrychkov.scrooge.component.report.periodtotal.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.report.periodtotal.internal.udf.PeriodTotalActor
import dev.aleksrychkov.scrooge.component.report.periodtotal.internal.udf.PeriodTotalEvent
import dev.aleksrychkov.scrooge.component.report.periodtotal.internal.udf.PeriodTotalReducer
import dev.aleksrychkov.scrooge.component.report.periodtotal.internal.udf.PeriodTotalState
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import kotlinx.coroutines.flow.StateFlow

internal class DefaultPeriodTotalComponent(
    componentContext: ComponentContext,
) : PeriodTotalComponentInternal, ComponentContext by componentContext {

    private val store: Store<PeriodTotalState, PeriodTotalEvent, Unit> by lazy {
        instanceKeeper.createStore(
            initialState = PeriodTotalState(),
            actor = PeriodTotalActor(),
            reducer = PeriodTotalReducer(),
        )
    }

    override val state: StateFlow<PeriodTotalState>
        get() = store.state

    override fun setPeriod(fromTimestamp: Long, toTimestamp: Long) {
        store.handle(
            PeriodTotalEvent.External.Load(
                fromTimestamp = fromTimestamp,
                toTimestamp = toTimestamp,
            )
        )
    }
}
