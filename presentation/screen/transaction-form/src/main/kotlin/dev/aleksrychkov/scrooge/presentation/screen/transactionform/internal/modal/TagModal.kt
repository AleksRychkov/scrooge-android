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
import dev.aleksrychkov.scrooge.component.tag.TagComponent
import dev.aleksrychkov.scrooge.component.tag.TagContent
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.TransactionFormComponentInternal
import kotlinx.coroutines.launch

@Composable
internal fun TagModal(
    component: TransactionFormComponentInternal,
) {
    val categorySlot = component.tagModal.subscribeAsState().value
    TagModal(
        slot = categorySlot,
        close = component::closeTagModal,
        select = component::addTag,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TagModal(
    slot: ChildSlot<*, TagComponent>,
    close: () -> Unit,
    select: (TagEntity) -> Unit,
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
            TagContent(
                modifier = Modifier.fillMaxSize(),
                component = component,
                callback = { tagEntity ->
                    tagEntity?.let(select)
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
