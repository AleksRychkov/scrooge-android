package dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.router.Router
import dev.aleksrychkov.scrooge.core.router.context.RouterComponentContext
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersComponent
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersSettings
import dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal.component.bycategory.ByCategoryComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.EnumSet

@Suppress("UnusedPrivateProperty")
internal class DefaultReportCategoryTotalComponent(
    componentContext: ComponentContext,
    private val filter: FilterEntity,
) : ReportCategoryTotalComponentInternal, ComponentContext by componentContext {

    private val filtersNavigation = SlotNavigation<FilterEntity>()

    private val router: Router by lazy {
        (componentContext as RouterComponentContext).router
    }

    private val _byCategoryComponent: ByCategoryComponent by lazy {
        ByCategoryComponent.Companion(
            componentContext = childContext("ReportCategoryTotalCategoryComponent"),
            filter = filter,
        )
    }

    private val _state = MutableStateFlow(
        ReportCategoryState(
            filter = filter,
            filtersName = "TODO",
        )
    )

    override val filtersModal: Value<ChildSlot<*, FiltersComponent>> =
        childSlot(
            source = filtersNavigation,
            serializer = null,
            handleBackButton = true,
            key = "ReportCategoryTotalFiltersModalSlot",
        ) { filter, childComponentContext ->
            FiltersComponent(
                componentContext = childComponentContext,
                filter = filter,
                settings = EnumSet.allOf(FiltersSettings::class.java),
            )
        }

    override val state: StateFlow<ReportCategoryState>
        get() = _state.asStateFlow()

    override val byCategoryComponent: ByCategoryComponent
        get() = _byCategoryComponent

    override fun openFiltersModal() {
        filtersNavigation.activate(state.value.filter)
    }

    override fun closeFiltersModal() {
        filtersNavigation.dismiss()
    }

    override fun setFilter(filter: FilterEntity) {
        _state.value = _state.value.copy(
            filter = filter,
            filtersName = "TODO",
        )
        _byCategoryComponent.setFilter(filter)
    }

    override fun onBackClicked() {
        router.close()
    }
}
