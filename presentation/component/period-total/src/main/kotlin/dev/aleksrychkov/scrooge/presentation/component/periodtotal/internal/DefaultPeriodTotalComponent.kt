package dev.aleksrychkov.scrooge.presentation.component.periodtotal.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.internal.udf.PeriodTotalActor
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.internal.udf.PeriodTotalEvent
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.internal.udf.PeriodTotalReducer
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.internal.udf.PeriodTotalState
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

    override fun setPeriod(period: PeriodTimestampEntity) {
        store.handle(PeriodTotalEvent.External.Load(period = period))
    }
}
