package dev.aleksrychkov.scrooge.presentation.component.tags

import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.router.slot.ChildSlot
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import kotlinx.collections.immutable.ImmutableSet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagSelectionModal(
    slot: ChildSlot<*, TagComponent>,
    close: () -> Unit,
    initialSelection: ImmutableSet<TagEntity>,
    callback: (Set<TagEntity>) -> Unit,
) {
    slot.child?.instance?.also { component ->
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
            TagSelectionContent(
                modifier = Modifier.fillMaxSize(),
                component = component,
                initialSelection = initialSelection,
                callback = callback,
            )
        }
    }
}
