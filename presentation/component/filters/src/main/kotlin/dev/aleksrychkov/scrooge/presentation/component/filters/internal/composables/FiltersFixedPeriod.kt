package dev.aleksrychkov.scrooge.presentation.component.filters.internal.composables

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Small
import dev.aleksrychkov.scrooge.core.designsystem.utils.reallyPerformHapticFeedback
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersSettings
import dev.aleksrychkov.scrooge.presentation.component.tags.composable.TagsRow
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Month
import java.util.EnumSet

@Suppress("LongParameterList")
@Composable
internal fun FiltersFixedPeriod(
    modifier: Modifier,
    settings: EnumSet<FiltersSettings>,
    allYears: ImmutableList<Int>,
    selectedYears: ImmutableList<Int>,
    allMonths: ImmutableList<String>,
    selectedMonths: ImmutableList<Int>,
    selectedTags: ImmutableSet<TagEntity>,
    onYearClicked: (Int) -> Unit,
    onYearLongClicked: (Int) -> Unit,
    onMonthClicked: (Int) -> Unit,
    onMonthLongClicked: (Int) -> Unit,
    removeTag: (TagEntity) -> Unit,
    openTagModal: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (settings.contains(FiltersSettings.Years)) {
            Years(
                modifier = Modifier.wrapContentWidth(),
                allYears = allYears,
                selectedYears = selectedYears,
                onYearClicked = onYearClicked,
                onYearLongClicked = onYearLongClicked,
            )
        }

        if (settings.contains(FiltersSettings.Months) && allMonths.isNotEmpty()) {
            Spacer(modifier = Modifier.height(Normal))

            Months(
                modifier = Modifier.fillMaxWidth(),
                isEnabled = selectedYears.size == 1,
                allMonths = allMonths,
                selectedMonths = selectedMonths,
                onMonthClicked = onMonthClicked,
                onMonthLongClicked = onMonthLongClicked,
            )
        }

        if (settings.contains(FiltersSettings.Tags)) {
            Spacer(modifier = Modifier.height(Normal))

            Tags(
                modifier = Modifier.fillMaxWidth(),
                selectedTags = selectedTags,
                removeTag = removeTag,
                addTag = openTagModal,
            )
        }
    }
}

@Composable
private fun Years(
    modifier: Modifier,
    allYears: ImmutableList<Int>,
    selectedYears: ImmutableList<Int>,
    onYearClicked: (Int) -> Unit,
    onYearLongClicked: (Int) -> Unit,
) {
    if (allYears.isEmpty()) return
    var selectedItemIndex = allYears.indexOf(selectedYears.last())
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
                OutlinedBox(
                    modifier = Modifier,
                    isEnabled = true,
                    onClick = { onYearClicked(year) },
                    onLongClick = { onYearLongClicked(year) }
                ) {
                    val color = if (selectedYears.contains(year)) {
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
    isEnabled: Boolean,
    allMonths: ImmutableList<String>,
    selectedMonths: ImmutableList<Int>,
    onMonthClicked: (Int) -> Unit,
    onMonthLongClicked: (Int) -> Unit,
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
            isEnabled = isEnabled,
            selectedMonths = selectedMonths,
            months = allMonths.take(3).toImmutableList(),
            monthsPad = 0,
            onMonthClicked = onMonthClicked,
            onMonthLongClicked = onMonthLongClicked,
        )

        MonthRow(
            modifier = Modifier.fillMaxWidth(),
            isEnabled = isEnabled,
            selectedMonths = selectedMonths,
            months = allMonths.subList(3, 6),
            monthsPad = 3,
            onMonthClicked = onMonthClicked,
            onMonthLongClicked = onMonthLongClicked,
        )

        MonthRow(
            modifier = Modifier.fillMaxWidth(),
            isEnabled = isEnabled,
            selectedMonths = selectedMonths,
            months = allMonths.subList(6, 9),
            monthsPad = 6,
            onMonthClicked = onMonthClicked,
            onMonthLongClicked = onMonthLongClicked,
        )

        MonthRow(
            modifier = Modifier.fillMaxWidth(),
            isEnabled = isEnabled,
            selectedMonths = selectedMonths,
            months = allMonths.subList(9, 12),
            monthsPad = 9,
            onMonthClicked = onMonthClicked,
            onMonthLongClicked = onMonthLongClicked,
        )
    }
}

@Composable
private fun MonthRow(
    modifier: Modifier,
    isEnabled: Boolean,
    selectedMonths: ImmutableList<Int>,
    months: ImmutableList<String>,
    monthsPad: Int,
    onMonthClicked: (Int) -> Unit,
    onMonthLongClicked: (Int) -> Unit,
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
            OutlinedBox(
                modifier = Modifier.weight(1f),
                isEnabled = isEnabled,
                onClick = { onMonthClicked(monthNumber) },
                onLongClick = { onMonthLongClicked(monthNumber) }
            ) {
                val color = if (selectedMonths.contains(monthNumber)) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onBackground
                }
                Text(
                    color = color,
                    text = month,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
private fun Tags(
    modifier: Modifier,
    selectedTags: ImmutableSet<TagEntity>,
    removeTag: (TagEntity) -> Unit,
    addTag: () -> Unit,
) {
    TagsRow(
        modifier
            .padding(horizontal = Large)
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = CardDefaults.shape,
            )
            .padding(horizontal = Large),
        tags = selectedTags,
        removeTag = removeTag,
        openTagModal = addTag,
    )
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

@Composable
private fun OutlinedBox(
    modifier: Modifier,
    isEnabled: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    val view = LocalView.current
    Box(
        modifier = modifier
            .padding(vertical = Small)
            .clip(ButtonDefaults.outlinedShape)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(
                    alpha = if (isEnabled) 1f else 0.25f,
                ),
                shape = ButtonDefaults.outlinedShape,
            )
            .combinedClickable(
                enabled = isEnabled,
                onClick = onClick,
                onLongClick = {
                    view.reallyPerformHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                    onLongClick()
                },
            )
            .padding(ButtonDefaults.ContentPadding),
        contentAlignment = Alignment.Center,
        content = content,
    )
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
                allYears = persistentListOf(2021, 2022, 2023, 2024, 2025),
                selectedYears = persistentListOf(2025),
                allMonths = Month.entries.map { it.name }.toImmutableList(),
                selectedMonths = persistentListOf(12),
                selectedTags = persistentSetOf(TagEntity.from("Tag 1")),
                onYearClicked = {},
                onYearLongClicked = {},
                onMonthClicked = {},
                onMonthLongClicked = {},
                openTagModal = {},
                removeTag = { _ -> },
            )
        }
    }
}
