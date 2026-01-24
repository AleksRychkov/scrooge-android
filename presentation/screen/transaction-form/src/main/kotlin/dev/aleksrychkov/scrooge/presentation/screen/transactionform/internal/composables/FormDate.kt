package dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.composables

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsInputTextFieldsColors
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsSecondaryCard
import dev.aleksrychkov.scrooge.core.designsystem.picker.WheelDatePickerDialog
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Small
import dev.aleksrychkov.scrooge.core.entity.Datestamp
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
internal fun FormDate(
    modifier: Modifier,
    datestamp: Datestamp,
    onDateSelected: (Long?) -> Unit,
) {
    var showModal by remember { mutableStateOf(false) }
    DsSecondaryCard(
        modifier.height(intrinsicSize = IntrinsicSize.Max)
    ) {
        val date = remember(datestamp) { datestamp.readableName() }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Small)
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
            colors = DsInputTextFieldsColors(),
            readOnly = true,
            onValueChange = { },
        )
    }

    if (showModal) {
        val timestamp = remember(datestamp) { datestamp.toInstant() }
        WheelDatePickerDialog(
            timestamp = timestamp,
            onDateSelected = onDateSelected,
            onDismiss = { showModal = false }
        )
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun FormContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            FormDate(
                modifier = Modifier.fillMaxWidth(),
                datestamp = Datestamp.now(),
                onDateSelected = {},
            )
        }
    }
}
