package dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.modal

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
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.presentaion.component.transactioncurrency.CurrencyComponent
import dev.aleksrychkov.scrooge.presentaion.component.transactioncurrency.CurrencyContent
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.TransactionFormComponentInternal
import kotlinx.coroutines.launch

@Composable
internal fun CurrencyModal(
    component: TransactionFormComponentInternal,
) {
    val currencySlot = component.currencyModal.subscribeAsState().value
    CurrencyModal(
        slot = currencySlot,
        close = component::closeCurrencyModal,
        select = component::selectCurrency,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencyModal(
    slot: ChildSlot<*, CurrencyComponent>,
    close: () -> Unit,
    select: (CurrencyEntity) -> Unit,
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
            CurrencyContent(
                modifier = Modifier.fillMaxSize(),
                component = component,
                callback = { currencyEntity ->
                    currencyEntity?.let(select)
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
