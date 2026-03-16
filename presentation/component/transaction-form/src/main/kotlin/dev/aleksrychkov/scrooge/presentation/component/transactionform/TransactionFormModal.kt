package dev.aleksrychkov.scrooge.presentation.component.transactionform

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.arkivanov.decompose.router.slot.ChildSlot
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables.FormTransactionType
import kotlinx.coroutines.launch
import dev.aleksrychkov.scrooge.core.resources.R as Resources

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
            dragHandle = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Icon(
                        modifier = Modifier
                            .clip(CircleShape)
                            .padding(Normal),
                        imageVector = Icons.Rounded.Close,
                        tint = Color.Transparent,
                        contentDescription = null,
                    )

                    FormTransactionType(
                        modifier = Modifier.padding(top = Normal),
                        transactionType = component.transactionType,
                    )

                    Icon(
                        modifier = Modifier
                            .clip(CircleShape)
                            .debounceClickable(onClick = onClose)
                            .padding(Normal),
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(Resources.string.close),
                    )
                }
            },
            sheetGesturesEnabled = false,
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
