package dev.aleksrychkov.scrooge.presentation.screen.transaction.internal.modal

import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.slot.ChildSlot
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersComponent
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersContent
import dev.aleksrychkov.scrooge.presentation.screen.transaction.internal.TransactionsComponentInternal
import kotlinx.coroutines.launch

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

@Suppress("UnusedParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FiltersModal(
    slot: ChildSlot<*, FiltersComponent>,
    close: () -> Unit,
    setFilter: (FilterEntity) -> Unit,
) {
    slot.child?.instance?.also { component ->
        val scope = rememberCoroutineScope()
        val modalBottomSheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        )
        ModalBottomSheet(
            onDismissRequest = close,
            modifier = Modifier
                .fillMaxSize()
                .displayCutoutPadding()
                .statusBarsPadding(),
            sheetState = modalBottomSheetState,
        ) {
            FiltersContent(
                modifier = Modifier.fillMaxSize(),
                component = component,
                callback = { filter ->
                    setFilter(filter)
                    scope.launch {
                        modalBottomSheetState.hide()
                    }.invokeOnCompletion {
                        if (!modalBottomSheetState.isVisible) close()
                    }
                }
            )
        }
    }
}
