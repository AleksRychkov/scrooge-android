package dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.modal

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
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.CategoryComponentInternal
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component.CreateCategoryComponent
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component.CreateCategoryContent
import kotlinx.coroutines.launch

@Composable
internal fun CreateCategoryModal(
    component: CategoryComponentInternal
) {
    val slot = component.createCategoryModal.subscribeAsState().value
    CreateCategoryModal(
        slot = slot,
        close = component::closeAddCategoryModal,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateCategoryModal(
    slot: ChildSlot<*, CreateCategoryComponent>,
    close: () -> Unit,
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
            CreateCategoryContent(
                modifier = Modifier.fillMaxSize(),
                component = component,
                onCloseCallback = {
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
