package dev.aleksrychkov.scrooge.component.transactionform.internal.composables

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import dev.aleksrychkov.scrooge.component.transactionform.internal.modal.DatePickerModal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import kotlin.time.Instant
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
internal fun FormDate(
    modifier: Modifier,
    timestamp: Instant,
    date: String,
    onDateSelected: (Long?) -> Unit,
) {
    var showModal by remember { mutableStateOf(false) }

    TextField(
        modifier = modifier
            .height(intrinsicSize = IntrinsicSize.Max)
            .padding(Normal)
            .pointerInput(date) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            },
        value = date,
        singleLine = true,
        label = {
            Text(stringResource(Resources.string.date))
        },
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            unfocusedTextColor = Color.Unspecified,
        ),
        readOnly = true,
        onValueChange = { },
    )

    if (showModal) {
        DatePickerModal(
            timestamp = timestamp,
            onDateSelected = onDateSelected,
            onDismiss = { showModal = false }
        )
    }
}
