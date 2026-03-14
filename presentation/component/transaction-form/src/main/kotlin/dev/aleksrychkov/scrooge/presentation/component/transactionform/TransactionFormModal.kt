package dev.aleksrychkov.scrooge.presentation.component.transactionform

import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.router.slot.ChildSlot
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionFormModal(
    slot: ChildSlot<*, TransactionFormComponent>,
    onClose: () -> Unit,
) {
    slot.child?.instance?.also { component ->
        val scope = rememberCoroutineScope()
        val modalBottomSheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        )
        ModalBottomSheet(
            onDismissRequest = onClose,
            modifier = Modifier
                .fillMaxSize()
                .displayCutoutPadding()
                .statusBarsPadding(),
            dragHandle = null,
            sheetState = modalBottomSheetState,
        ) {
            TransactionFormContent(
                modifier = Modifier.fillMaxSize(),
                component = component,
                onDone = {
                    scope.launch {
                        modalBottomSheetState.hide()
                    }.invokeOnCompletion {
                        if (!modalBottomSheetState.isVisible) onClose()
                    }
                }
            )
        }
    }
}
