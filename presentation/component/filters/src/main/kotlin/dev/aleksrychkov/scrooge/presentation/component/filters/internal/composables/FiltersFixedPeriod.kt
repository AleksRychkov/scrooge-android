package dev.aleksrychkov.scrooge.presentation.component.filters.internal.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal2X
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersSettings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Month
import java.util.EnumSet
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
internal fun FiltersFixedPeriod(
    modifier: Modifier,
    settings: EnumSet<FiltersSettings>,
    allYears: ImmutableList<Int>,
    selectedYear: Int,
    allMonths: ImmutableList<String>,
    selectedMonth: Int,
    allTags: ImmutableSet<String>,
    selectedTags: ImmutableSet<String>,
    onYearClicked: (Int) -> Unit,
    onMonthClicked: (Int) -> Unit,
    toggleTag: (String) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (settings.contains(FiltersSettings.Years)) {
            Years(
                modifier = Modifier.wrapContentWidth(),
                allYears = allYears,
                selectedYear = selectedYear,
                onYearClicked = onYearClicked,
            )
        }

        if (settings.contains(FiltersSettings.Months) && allMonths.isNotEmpty()) {
            Spacer(modifier = Modifier.height(Normal))

            Months(
                modifier = Modifier.fillMaxWidth(),
                allMonths = allMonths,
                selectedMonth = selectedMonth,
                onMonthClicked = onMonthClicked,
            )
        }

        if (settings.contains(FiltersSettings.Tags) && allTags.isNotEmpty()) {
            Spacer(modifier = Modifier.height(Normal))

            Tags(
                modifier = Modifier.fillMaxWidth(),
                tags = allTags,
                selectedTags = selectedTags,
                toggleTag = toggleTag,
            )
        }
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
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Large)
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = CardDefaults.shape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        LazyRow(
            state = listState,
            contentPadding = PaddingValues(Normal),
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
    }

    LaunchedEffect(Unit) {
        listState.animateScrollAndCentralizeItem(selectedItemIndex)
    }
}

@Suppress("MagicNumber")
@Composable
private fun Months(
    modifier: Modifier,
    allMonths: ImmutableList<String>,
    selectedMonth: Int,
    onMonthClicked: (Int) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Large)
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = CardDefaults.shape,
            )
            .padding(vertical = Normal),
    ) {
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

@Composable
private fun Tags(
    modifier: Modifier,
    tags: ImmutableSet<String>,
    selectedTags: ImmutableSet<String>,
    toggleTag: (String) -> Unit,
) {
    FlowRow(
        modifier = modifier
            .padding(horizontal = Large)
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = CardDefaults.shape,
            )
            .padding(Normal)
            .verticalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(space = Normal),
        itemVerticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier,
            imageVector = ImageVector.vectorResource(Resources.drawable.ic_tag_24px),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
        )

        tags.forEach { tag ->
            val isSelected = selectedTags.contains(tag)
            InputChip(
                modifier = Modifier,
                selected = isSelected,
                colors = InputChipDefaults.inputChipColors().copy(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                ),
                label = {
                    Text(
                        modifier = Modifier,
                        text = tag,
                    )
                },
                shape = RoundedCornerShape(Normal2X),
                onClick = { toggleTag(tag) },
            )
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
                settings = EnumSet.allOf(FiltersSettings::class.java),
                allYears = persistentListOf(2024, 2025, 2026, 2027, 2028, 2029),
                selectedYear = 2025,
                allMonths = Month.entries.map { it.name }.toImmutableList(),
                selectedMonth = 12,
                allTags = persistentSetOf("Tag 1", "Tag 2"),
                selectedTags = persistentSetOf("Tag 1"),
                onYearClicked = {},
                onMonthClicked = {},
                toggleTag = { _ -> },
            )
        }
    }
}
