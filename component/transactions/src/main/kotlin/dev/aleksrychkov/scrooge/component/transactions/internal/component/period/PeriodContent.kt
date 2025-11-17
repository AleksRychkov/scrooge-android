package dev.aleksrychkov.scrooge.component.transactions.internal.component.period

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Small
import kotlinx.datetime.Month
import kotlinx.datetime.number
import kotlin.time.Instant

@Composable
internal fun PeriodContent(
    modifier: Modifier,
    component: PeriodComponent,
    callback: (Instant) -> Unit,
) {
    val state by component.state.collectAsStateWithLifecycle()

    PeriodContent(
        modifier = modifier,
        state = state,
        onIncrementYearClicked = component::incrementYear,
        onDecrementYearClicked = component::decrementYear,
        onMonthClicked = { month ->
            val instant = component.monthSelected(month = month)
            callback(instant)
        },
    )
}

@Composable
internal fun PeriodContent(
    modifier: Modifier,
    state: PeriodState,
    onIncrementYearClicked: () -> Unit,
    onDecrementYearClicked: () -> Unit,
    onMonthClicked: (Int) -> Unit,
) {
    Column(modifier = modifier) {
        YearRow(
            modifier = Modifier.fillMaxWidth(),
            year = state.selectedYear.toString(),
            onIncrementYearClicked = onIncrementYearClicked,
            onDecrementYearClicked = onDecrementYearClicked,
        )

        val isSelectedYearInitial = state.initialYear == state.selectedYear

        MonthRow(
            modifier = Modifier.fillMaxWidth(),
            months = arrayOf(Month.JANUARY, Month.FEBRUARY, Month.MARCH),
            selectedMonth = state.selectedMonth,
            isSelectedYearInitial = isSelectedYearInitial,
            onMonthClicked = onMonthClicked,
        )
        MonthRow(
            modifier = Modifier.fillMaxWidth(),
            months = arrayOf(Month.APRIL, Month.MAY, Month.JUNE),
            selectedMonth = state.selectedMonth,
            isSelectedYearInitial = isSelectedYearInitial,
            onMonthClicked = onMonthClicked,
        )
        MonthRow(
            modifier = Modifier.fillMaxWidth(),
            months = arrayOf(Month.JULY, Month.AUGUST, Month.SEPTEMBER),
            selectedMonth = state.selectedMonth,
            isSelectedYearInitial = isSelectedYearInitial,
            onMonthClicked = onMonthClicked,
        )
        MonthRow(
            modifier = Modifier.fillMaxWidth(),
            months = arrayOf(Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER),
            selectedMonth = state.selectedMonth,
            isSelectedYearInitial = isSelectedYearInitial,
            onMonthClicked = onMonthClicked,
        )
    }
}

@Composable
private fun YearRow(
    modifier: Modifier,
    year: String,
    onIncrementYearClicked: () -> Unit,
    onDecrementYearClicked: () -> Unit,
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .padding(horizontal = Large),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .padding(horizontal = Normal, vertical = Small),
            onClick = onDecrementYearClicked,
        ) {
            Icon(
                Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = null,
            )
        }
        Text(
            modifier = Modifier.padding(Normal),
            text = year,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
        )

        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .padding(horizontal = Normal, vertical = Small),
            onClick = onIncrementYearClicked,
        ) {
            Icon(
                Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun MonthRow(
    modifier: Modifier,
    selectedMonth: Int,
    months: Array<Month>,
    isSelectedYearInitial: Boolean,
    onMonthClicked: (Int) -> Unit,
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .padding(horizontal = Large),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Normal)
    ) {
        months.forEach { month ->
            OutlinedButton(
                modifier = Modifier.weight(weight = 1f, fill = true),
                onClick = {
                    onMonthClicked(month.number)
                }
            ) {
                val color = if (isSelectedYearInitial && month.number == selectedMonth) {
                    MaterialTheme.colorScheme.primary
                } else {
                    Color.Unspecified
                }
                Text(
                    color = color,
                    text = month.name.lowercase().replaceFirstChar { it.uppercase() },
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }
        }
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun FormContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            PeriodContent(
                modifier = Modifier,
                state = PeriodState(),
                onIncrementYearClicked = {},
                onDecrementYearClicked = {},
                onMonthClicked = {},
            )
        }
    }
}
