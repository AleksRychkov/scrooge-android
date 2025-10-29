package dev.aleksrychkov.scrooge.component.transactionform.internal.modal

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
import dev.aleksrychkov.scrooge.component.category.CategoryComponent
import dev.aleksrychkov.scrooge.component.category.CategoryContent
import dev.aleksrychkov.scrooge.component.transactionform.internal.TransactionFormComponentInternal
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import kotlinx.coroutines.launch

@Composable
internal fun CategoryModal(
    component: TransactionFormComponentInternal,
) {
    val categorySlot = component.categoryModal.subscribeAsState().value
    CategoryModal(
        slot = categorySlot,
        close = component::closeCategoryModal,
        select = component::setCategory,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryModal(
    slot: ChildSlot<*, CategoryComponent>,
    close: () -> Unit,
    select: (CategoryEntity) -> Unit,
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
            CategoryContent(
                modifier = Modifier.fillMaxSize(),
                component = component,
                callback = { categoryEntity ->
                    categoryEntity?.let(select)
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
