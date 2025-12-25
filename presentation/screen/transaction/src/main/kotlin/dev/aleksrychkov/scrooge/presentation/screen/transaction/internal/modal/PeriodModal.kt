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
import dev.aleksrychkov.scrooge.presentation.screen.transaction.internal.TransactionsComponentInternal
import dev.aleksrychkov.scrooge.presentation.screen.transaction.internal.component.period.PeriodComponent
import dev.aleksrychkov.scrooge.presentation.screen.transaction.internal.component.period.PeriodContent
import kotlinx.coroutines.launch
import kotlin.time.Instant

@Composable
internal fun PeriodModal(
    component: TransactionsComponentInternal,
) {
    val periodSlot = component.periodModal.subscribeAsState().value
    PeriodModal(
        slot = periodSlot,
        close = component::closePeriodModal,
        setPeriod = component::setPeriod,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PeriodModal(
    slot: ChildSlot<*, PeriodComponent>,
    close: () -> Unit,
    setPeriod: (Instant) -> Unit,
) {
    slot.child?.instance?.also { component ->
        val scope = rememberCoroutineScope()
        val modalBottomSheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = close,
            modifier = Modifier
                .fillMaxSize()
                .displayCutoutPadding()
                .statusBarsPadding(),
            sheetState = modalBottomSheetState,
        ) {
            PeriodContent(
                modifier = Modifier.fillMaxSize(),
                component = component,
                callback = { instant ->
                    setPeriod(instant)
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
