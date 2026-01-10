package dev.aleksrychkov.scrooge.presentation.component.filters.internal.composables

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsInputTextFieldsColors
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsSecondaryCard
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Small
import dev.aleksrychkov.scrooge.core.designsystem.utils.reallyPerformHapticFeedback
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersSettings
import dev.aleksrychkov.scrooge.presentation.component.tags.composable.TagsRow
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Month
import java.util.EnumSet
import dev.aleksrychkov.scrooge.core.resources.R as Resources

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
    category: CategoryEntity?,
    selectedType: TransactionType?,
    onYearClicked: (Int) -> Unit,
    onYearLongClicked: (Int) -> Unit,
    onMonthClicked: (Int) -> Unit,
    onMonthLongClicked: (Int) -> Unit,
    removeTag: (TagEntity) -> Unit,
    openTagModal: () -> Unit,
    openCategoryModal: () -> Unit,
    removeCategory: () -> Unit,
    onTransactionTypeSelected: (TransactionType?) -> Unit,
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

        if (settings.contains(FiltersSettings.TransactionType)) {
            Spacer(modifier = Modifier.height(Normal))

            TransactionType(
                modifier = Modifier.fillMaxWidth(),
                selectedType = selectedType,
                onTransactionTypeSelected = onTransactionTypeSelected,
            )
        }

        if (settings.contains(FiltersSettings.Category)) {
            Spacer(modifier = Modifier.height(Normal))

            Category(
                modifier = Modifier.fillMaxWidth(),
                category = category,
                addCategory = openCategoryModal,
                removeCategory = removeCategory,
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
    if (allYears.isEmpty() || selectedYears.isEmpty()) return
    var selectedItemIndex = allYears.indexOf(selectedYears.last())
    if (selectedItemIndex < 0) selectedItemIndex = allYears.size / 2
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = selectedItemIndex,
    )
    DsSecondaryCard(
        modifier = modifier.padding(horizontal = Large)
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = Large),
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
    DsSecondaryCard(
        modifier = modifier.padding(horizontal = Large)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(Normal)
        )
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
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(Normal)
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
    DsSecondaryCard(
        modifier = modifier.padding(horizontal = Large)
    ) {
        TagsRow(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = Large, vertical = Normal),
            tags = selectedTags,
            removeTag = removeTag,
            openTagModal = addTag,
        )
    }
}

@Composable
private fun Category(
    modifier: Modifier,
    category: CategoryEntity?,
    addCategory: () -> Unit,
    removeCategory: () -> Unit
) {
    DsSecondaryCard(
        modifier = modifier
            .padding(horizontal = Large)
            .height(intrinsicSize = IntrinsicSize.Max)
    ) {
        val focusManager = LocalFocusManager.current
        Box {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = category?.name.orEmpty(),
                singleLine = true,
                label = {
                    Text(stringResource(Resources.string.category))
                },
                colors = DsInputTextFieldsColors(),
                readOnly = true,
                trailingIcon = {
                    if (category != null) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .debounceClickable {
                                    focusManager.clearFocus(force = true)
                                    removeCategory()
                                }
                        )
                    }
                },
                onValueChange = { },
            )
            if (category == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .debounceClickable(onClick = addCategory)
                )
            }
        }
    }
}

@Composable
private fun TransactionType(
    modifier: Modifier,
    selectedType: TransactionType? = null,
    onTransactionTypeSelected: (TransactionType?) -> Unit,
) {
    DsSecondaryCard(
        modifier = modifier.padding(horizontal = Large)
    ) {
        Row(
            modifier = modifier
                .height(IntrinsicSize.Min)
                .padding(horizontal = Large, vertical = Normal)
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Normal)
        ) {
            TransactionTypeItem(
                modifier = Modifier.wrapContentWidth(),
                isSelected = selectedType == null,
                name = stringResource(Resources.string.all)
            ) { onTransactionTypeSelected(null) }

            TransactionTypeItem(
                modifier = Modifier.wrapContentWidth(),
                isSelected = selectedType == TransactionType.Expense,
                name = stringResource(Resources.string.expenses)
            ) { onTransactionTypeSelected(TransactionType.Expense) }

            TransactionTypeItem(
                modifier = Modifier.wrapContentWidth(),
                isSelected = selectedType == TransactionType.Income,
                name = stringResource(Resources.string.incomes)
            ) { onTransactionTypeSelected(TransactionType.Income) }
        }
    }
}

@Composable
private fun TransactionTypeItem(
    modifier: Modifier,
    isSelected: Boolean,
    name: String,
    onClick: () -> Unit,
) {
    OutlinedBox(
        modifier = modifier,
        isEnabled = true,
        onClick = onClick,
    ) {
        val color = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onBackground
        }
        Text(
            color = color,
            text = name,
            maxLines = 1,
        )
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

@Composable
private fun OutlinedBox(
    modifier: Modifier,
    isEnabled: Boolean,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
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
                    if (onLongClick != null) {
                        view.reallyPerformHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                        onLongClick()
                    }
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
                selectedType = null,
                category = null,
                onYearClicked = {},
                onYearLongClicked = {},
                onMonthClicked = {},
                onMonthLongClicked = {},
                openTagModal = {},
                removeTag = { _ -> },
                openCategoryModal = {},
                removeCategory = {},
                onTransactionTypeSelected = { _ -> },
            )
        }
    }
}
