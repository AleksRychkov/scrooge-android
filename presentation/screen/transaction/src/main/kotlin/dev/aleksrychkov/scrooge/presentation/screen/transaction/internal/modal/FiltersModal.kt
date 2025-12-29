package dev.aleksrychkov.scrooge.presentation.screen.transaction.internal.modal

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.slot.ChildSlot
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersBottomSheetModal
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersComponent
import dev.aleksrychkov.scrooge.presentation.screen.transaction.internal.TransactionsComponentInternal

@Composable
internal fun FiltersModal(
    component: TransactionsComponentInternal,
) {
    val periodSlot = component.filtersModal.subscribeAsState().value
    FiltersModal(
        slot = periodSlot,
        close = component::closeFiltersModal,
        setFilter = component::setFilter,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FiltersModal(
    slot: ChildSlot<*, FiltersComponent>,
    close: () -> Unit,
    setFilter: (FilterEntity) -> Unit,
) {
    slot.child?.instance?.also { component ->
        FiltersBottomSheetModal(
            component = component,
            close = close,
            setFilter = setFilter,
        )
    }
}
