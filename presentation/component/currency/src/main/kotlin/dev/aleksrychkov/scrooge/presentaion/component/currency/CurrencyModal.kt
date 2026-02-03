package dev.aleksrychkov.scrooge.presentaion.component.currency

import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyModal(
    component: CurrencyComponent,
    onDismiss: () -> Unit,
    callback: (CurrencyEntity) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxSize()
            .displayCutoutPadding()
            .statusBarsPadding(),
        sheetState = modalBottomSheetState,
    ) {
        CurrencyContent(
            modifier = Modifier.fillMaxSize(),
            component = component,
            callback = { currencyEntity ->
                currencyEntity?.let(callback)
                scope.launch {
                    modalBottomSheetState.hide()
                }.invokeOnCompletion {
                    if (!modalBottomSheetState.isVisible) onDismiss()
                }
            }
        )
    }
}
