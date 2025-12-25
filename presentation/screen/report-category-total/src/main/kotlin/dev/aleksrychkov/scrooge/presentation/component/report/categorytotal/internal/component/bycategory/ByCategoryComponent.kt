package dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal.component.bycategory

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal.component.bycategory.udf.ByCategoryActor
import dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal.component.bycategory.udf.ByCategoryEvent
import dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal.component.bycategory.udf.ByCategoryReducer
import dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal.component.bycategory.udf.ByCategoryState
import kotlinx.coroutines.flow.StateFlow

internal interface ByCategoryComponent {
    companion object Companion {
        operator fun invoke(
            componentContext: ComponentContext,
            period: PeriodTimestampEntity,
        ): ByCategoryComponent = DefaultByCategoryComponent(
            componentContext = componentContext,
            period = period,
        )
    }

    val state: StateFlow<ByCategoryState>

    fun setTransactionType(type: Int)
}

private class DefaultByCategoryComponent(
    componentContext: ComponentContext,
    period: PeriodTimestampEntity
) : ByCategoryComponent, ComponentContext by componentContext {

    private val store: Store<ByCategoryState, ByCategoryEvent, Unit> by lazy {
        instanceKeeper.createStore(
            initialState = ByCategoryState(period = period),
            actor = ByCategoryActor.Companion(),
            reducer = ByCategoryReducer(),
            startEvent = ByCategoryEvent.External.Load(period),
        )
    }

    override val state: StateFlow<ByCategoryState>
        get() = store.state

    override fun setTransactionType(type: Int) {
        store.handle(ByCategoryEvent.External.SetType(type))
    }
}
