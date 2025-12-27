package dev.aleksrychkov.scrooge.presentation.component.filters.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersActor
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersEvent
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersReducer
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersState
import kotlinx.coroutines.flow.StateFlow

internal class DefaultFiltersComponent(
    componentContext: ComponentContext,
    filter: FilterEntity,
) : FiltersComponentInternal, ComponentContext by componentContext {

    private val store: Store<FiltersState, FiltersEvent, Unit> by lazy {
        instanceKeeper.createStore(
            initialState = FiltersState(),
            actor = FiltersActor(),
            reducer = FiltersReducer(),
            startEvent = FiltersEvent.External.Init(filter = filter),
        )
    }

    override val state: StateFlow<FiltersState>
        get() = store.state

    override fun onYearClicked(year: Int) {
        store.handle(FiltersEvent.External.YearClicked(year = year))
    }

    override fun onMonthClicked(month: Int) {
        store.handle(FiltersEvent.External.MonthClicked(month = month))
    }
}
