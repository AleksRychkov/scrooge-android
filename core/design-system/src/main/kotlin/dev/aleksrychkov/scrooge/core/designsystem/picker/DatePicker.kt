@file:Suppress("All")

package dev.aleksrychkov.scrooge.core.designsystem.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsWheel
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal2X
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until
import kotlin.math.min
import kotlin.time.Clock
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Suppress("ObjectPropertyName")
private var _today: LocalDate? = null
private val today: LocalDate
    get() {
        if (_today == null) {
            _today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        }
        return _today!!
    }

private var _years: ImmutableList<Int>? = null
private val years: ImmutableList<Int>
    get() {
        if (_years == null) {
            val to = today.year + 1
            val from = to - 100
            _years = (from..to).toImmutableList()
        }
        return _years!!
    }

@Composable
fun DatePicker(
    modifier: Modifier,
    selectedDate: LocalDate? = null,
    onDateSelected: (LocalDate) -> Unit,
) {
    var localDate by remember { mutableStateOf(selectedDate ?: today) }
    LaunchedEffect(Unit) {
        snapshotFlow { localDate }
            .distinctUntilChanged()
            .onEach {
                onDateSelected(it)
            }
            .launchIn(this)
    }

    Box(modifier = modifier) {

        Indicator()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DaysWheel(
                modifier = Modifier.weight(weight = 1f),
                selectedDate = localDate,
            ) { day ->
                localDate = LocalDate(year = localDate.year, month = localDate.month, day = day)
            }

            MonthsWheel(
                modifier = Modifier.weight(weight = 1f),
                selectedDate = localDate,
            ) { month ->
                val daysInMonth =
                    LocalDate(year = localDate.year, month = month, day = 1).daysInMonth()
                val day = min(daysInMonth, localDate.day)
                localDate = LocalDate(year = localDate.year, month = month, day = day)
            }

            YearsWheel(
                modifier = Modifier.weight(weight = 1f),
                selectedDate = localDate,
            ) { year ->
                localDate = LocalDate(year = year, month = localDate.month, day = localDate.day)
            }
        }


    }
}

@Composable
private fun BoxScope.Indicator() {
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val wheelLabelFontSize = MaterialTheme.typography.titleLarge.fontSize
    val itemHeight = remember {
        val result = textMeasurer.measure(
            text = AnnotatedString("Sample"),
            style = TextStyle(fontSize = wheelLabelFontSize)
        )
        with(density) {
            result.size.height.toDp() + Normal2X
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(itemHeight)
            .background(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f),
                shape = RoundedCornerShape(50),
            )
            .align(Alignment.Center),
    )
}

@Composable
private fun DaysWheel(
    modifier: Modifier,
    selectedDate: LocalDate,
    onDaySelected: (Int) -> Unit,
) {
    val days = remember(selectedDate) {
        (1..selectedDate.daysInMonth()).toImmutableList()
    }
    DsWheel(
        modifier = modifier,
        data = days,
        selectedItem = selectedDate.day,
        onItemSelected = onDaySelected,
    )
}

@Composable
private fun MonthsWheel(
    modifier: Modifier,
    selectedDate: LocalDate,
    onMonthSelected: (Int) -> Unit,
) {
    val monthsString = stringArrayResource(Resources.array.month_names)
    val monthsIndexes = remember {
        (0..Month.DECEMBER.ordinal).toImmutableList()
    }
    DsWheel(
        modifier = modifier,
        data = monthsIndexes,
        selectedItem = selectedDate.month.ordinal,
        labelAsString = { monthsString[this] },
        onItemSelected = {
            onMonthSelected(it + 1)
        }
    )
}

@Composable
private fun YearsWheel(
    modifier: Modifier,
    selectedDate: LocalDate,
    onYearSelected: (Int) -> Unit,
) {
    DsWheel(
        modifier = modifier,
        data = years,
        selectedItem = selectedDate.year,
        onItemSelected = onYearSelected,
    )
}

private fun LocalDate.daysInMonth(): Int {
    val start = LocalDate(year, month, 1)
    val end = start.plus(1, DateTimeUnit.MONTH)
    return start.until(end, DateTimeUnit.DAY).toInt()
}

@Preview
@Composable
private fun PreviewDatePicker() {
    AppTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            DatePicker(
                modifier = Modifier.fillMaxWidth(),
                selectedDate = LocalDate(2026, 1, 21),
            ) {}
        }
    }
}
