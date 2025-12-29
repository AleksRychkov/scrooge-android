package dev.aleksrychkov.scrooge.presentation.component.filters

import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersBottomSheetModal(
    component: FiltersComponent,
    close: () -> Unit,
    setFilter: (FilterEntity) -> Unit,
) {
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
