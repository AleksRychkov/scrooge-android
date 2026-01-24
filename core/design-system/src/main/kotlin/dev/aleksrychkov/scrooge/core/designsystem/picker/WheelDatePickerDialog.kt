package dev.aleksrychkov.scrooge.core.designsystem.picker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WheelDatePickerDialog(
    timestamp: Instant,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = null,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(topEnd = Normal, topStart = Normal),
        ) {
            Content(
                modifier = Modifier.fillMaxWidth(),
                timestamp = timestamp,
                onDateSelected = onDateSelected,
                onDismiss = onDismiss,
            )
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    timestamp: Instant,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
) {
    var selectedDate by remember { mutableLongStateOf(timestamp.toEpochMilliseconds()) }
    Column(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(Large))
        DatePicker(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Large),
            selectedDate = timestamp.toLocalDateTime(TimeZone.currentSystemDefault()).date,
            onDateSelected = {
                selectedDate = it
                    .atStartOfDayIn(TimeZone.currentSystemDefault())
                    .toEpochMilliseconds()
            }
        )

        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Large, vertical = Normal),
            onClick = {
                onDismiss.invoke()
                onDateSelected(selectedDate)
            },
        ) {
            Text(stringResource(Resources.string.apply))
        }
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun PreviewDateModalContent() {
    AppTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Content(
                modifier = Modifier.fillMaxWidth(),
                timestamp = Clock.System.now(),
                onDateSelected = { _ -> },
                onDismiss = {},
            )
        }
    }
}
