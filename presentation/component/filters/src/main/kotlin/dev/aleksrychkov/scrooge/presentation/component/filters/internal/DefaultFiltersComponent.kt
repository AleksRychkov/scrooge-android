package dev.aleksrychkov.scrooge.presentation.component.filters.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersSettings
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersActor
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersEffect
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersEvent
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersReducer
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersState
import dev.aleksrychkov.scrooge.presentation.component.transactiontag.TagComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.util.EnumSet

internal class DefaultFiltersComponent(
    componentContext: ComponentContext,
    filter: FilterEntity,
    settings: EnumSet<FiltersSettings>,
) : FiltersComponentInternal, ComponentContext by componentContext {

    private val tagNavigation = SlotNavigation<Unit>()

    private val store: Store<FiltersState, FiltersEvent, FiltersEffect> by lazy {
        instanceKeeper.createStore(
            initialState = FiltersState(),
            actor = FiltersActor(),
            reducer = FiltersReducer(),
            startEvent = FiltersEvent.External.Init(filter = filter, settings = settings),
        )
    }

    override val effects: Flow<FiltersEffect>
        get() = store.effects

    override val state: StateFlow<FiltersState>
        get() = store.state

    override fun onYearClicked(year: Int) {
        store.handle(FiltersEvent.External.YearClicked(year = year))
    }

    override fun onYearLongClicked(year: Int) {
        store.handle(FiltersEvent.External.YearClicked(year = year, isLongClick = true))
    }

    override fun onMonthClicked(month: Int) {
        store.handle(FiltersEvent.External.MonthClicked(month = month))
    }

    override fun onMonthLongClicked(month: Int) {
        store.handle(FiltersEvent.External.MonthClicked(month = month, isLongClick = true))
    }

    // region Tag
    override val tagModal: Value<ChildSlot<*, TagComponent>> =
        childSlot(
            source = tagNavigation,
            serializer = null,
            handleBackButton = true,
            key = "FiltersComponentTagModalSlot",
        ) { _, childComponentContext ->
            TagComponent(
                componentContext = childComponentContext,
                isEditable = false,
            )
        }

    override fun removeTag(tag: TagEntity) {
        store.handle(FiltersEvent.External.RemoveTag(tag = tag))
    }

    override fun openTagModal() {
        tagNavigation.activate(Unit)
    }

    override fun closeTagModal() {
        tagNavigation.dismiss()
    }

    override fun addTag(tag: TagEntity) {
        store.handle(FiltersEvent.External.AddTag(tag = tag))
    }
    // endregion Tag

    override fun resetFilters() {
        store.handle(FiltersEvent.External.Reset)
    }
}
