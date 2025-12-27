package dev.aleksrychkov.scrooge.presentation.component.filters.internal.composables

import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Month

@Composable
internal fun FiltersFixedPeriod(
    modifier: Modifier,
    allYears: ImmutableList<Int>,
    selectedYear: Int,
    allMonths: ImmutableList<String>,
    selectedMonth: Int,
    onYearClicked: (Int) -> Unit,
    onMonthClicked: (Int) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Years(
            modifier = Modifier.wrapContentWidth(),
            allYears = allYears,
            selectedYear = selectedYear,
            onYearClicked = onYearClicked,
        )

        Spacer(modifier = Modifier.height(Normal))

        Months(
            allMonths = allMonths,
            selectedMonth = selectedMonth,
            onMonthClicked = onMonthClicked,
        )
    }
}

@Composable
private fun Years(
    modifier: Modifier,
    allYears: ImmutableList<Int>,
    selectedYear: Int,
    onYearClicked: (Int) -> Unit,
) {
    if (allYears.isEmpty()) return
    var selectedItemIndex = allYears.indexOf(selectedYear)
    if (selectedItemIndex < 0) selectedItemIndex = allYears.size / 2
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = selectedItemIndex,
    )
    LazyRow(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(
            start = Large,
            end = Large,
        ),
        horizontalArrangement = Arrangement.spacedBy(Normal),
    ) {
        items(
            items = allYears,
            key = { it }
        ) { year ->
            OutlinedButton(
                modifier = Modifier.padding(),
                onClick = {
                    onYearClicked(year)
                }
            ) {
                val color = if (year == selectedYear) {
                    MaterialTheme.colorScheme.primary
                } else {
                    Color.Unspecified
                }
                Text(
                    color = color,
                    text = year.toString(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        listState.animateScrollAndCentralizeItem(selectedItemIndex)
    }
}

@Suppress("MagicNumber")
@Composable
private fun Months(
    allMonths: ImmutableList<String>,
    selectedMonth: Int,
    onMonthClicked: (Int) -> Unit,
) {
    if (allMonths.isEmpty()) return

    MonthRow(
        modifier = Modifier.fillMaxWidth(),
        selectedMonth = selectedMonth,
        months = allMonths.take(3).toImmutableList(),
        monthsPad = 0,
        onMonthClicked = onMonthClicked,
    )

    MonthRow(
        modifier = Modifier.fillMaxWidth(),
        selectedMonth = selectedMonth,
        months = allMonths.subList(3, 6),
        monthsPad = 3,
        onMonthClicked = onMonthClicked,
    )

    MonthRow(
        modifier = Modifier.fillMaxWidth(),
        selectedMonth = selectedMonth,
        months = allMonths.subList(6, 9),
        monthsPad = 6,
        onMonthClicked = onMonthClicked,
    )

    MonthRow(
        modifier = Modifier.fillMaxWidth(),
        selectedMonth = selectedMonth,
        months = allMonths.subList(9, 12),
        monthsPad = 9,
        onMonthClicked = onMonthClicked,
    )
}

@Composable
private fun MonthRow(
    modifier: Modifier,
    selectedMonth: Int,
    months: ImmutableList<String>,
    monthsPad: Int,
    onMonthClicked: (Int) -> Unit,
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .padding(horizontal = Large),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Normal)
    ) {
        months.forEachIndexed { index, month ->
            val monthNumber = index + 1 + monthsPad
            OutlinedButton(
                modifier = Modifier.weight(weight = 1f, fill = true),
                onClick = {
                    onMonthClicked(monthNumber)
                }
            ) {
                val color = if (monthNumber == selectedMonth) {
                    MaterialTheme.colorScheme.primary
                } else {
                    Color.Unspecified
                }
                Text(
                    color = color,
                    text = month,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }
        }
    }
}

private suspend fun LazyListState.animateScrollAndCentralizeItem(index: Int) {
    animateScrollToItem(index, -layoutInfo.viewportEndOffset / 2)
    val itemInfo = layoutInfo.visibleItemsInfo.firstOrNull { it.index == index } ?: return
    val viewportCenter =
        layoutInfo.viewportStartOffset + (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2
    val itemCenter = itemInfo.offset + itemInfo.size / 2
    val difference = itemCenter - viewportCenter
    animateScrollBy(difference.toFloat())
}

@Preview
@Composable
@Suppress("UnusedPrivateMember", "MagicNumber")
private fun ContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            FiltersFixedPeriod(
                modifier = Modifier,
                allYears = persistentListOf(2024, 2025, 2026),
                selectedYear = 2025,
                allMonths = Month.entries.map { it.name }.toImmutableList(),
                selectedMonth = 12,
                onYearClicked = {},
                onMonthClicked = {},
            )
        }
    }
}
