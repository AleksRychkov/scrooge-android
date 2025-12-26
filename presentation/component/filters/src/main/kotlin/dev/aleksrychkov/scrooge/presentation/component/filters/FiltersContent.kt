@file:Suppress("All")

package dev.aleksrychkov.scrooge.presentation.component.filters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large2X
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Small
import dev.aleksrychkov.scrooge.core.designsystem.theme.Tinny
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.FiltersComponentInternal
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun FiltersContent(
    modifier: Modifier,
    component: FiltersComponent,
    callback: (FilterEntity) -> Unit,
) {
    FiltersContent(
        modifier = modifier,
        component = component as FiltersComponentInternal,
    )
}

@Composable
private fun FiltersContent(
    modifier: Modifier,
    component: FiltersComponentInternal,
) {
    val state by component.state.collectAsStateWithLifecycle()

    FiltersContent(
        modifier = modifier,
        state = state,
        onDateClicked = component::onDateClicked,
    )
}

@Composable
private fun FiltersContent(
    modifier: Modifier,
    state: FiltersState,
    onDateClicked: (Int, Int, Int) -> Unit,
) {
    Column(modifier = modifier) {
        PeriodGrid(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            startSelection = state.startSelection,
            endSelection = state.endSelection,
            grid = state.grid,
            onDateClicked = onDateClicked,
        )
        ExtendedFloatingActionButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Large)
                .padding(bottom = Large2X),
            onClick = { },
        ) {
            Text(stringResource(Resources.string.submit))
        }
    }
}

@Composable
private fun PeriodGrid(
    modifier: Modifier,
    startSelection: FiltersState.SelectionDate?,
    endSelection: FiltersState.SelectionDate?,
    grid: ImmutableList<FiltersState.GridItem>,
    onDateClicked: (Int, Int, Int) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier.padding(horizontal = Large),
        columns = GridCells.Fixed(count = 7),
        contentPadding = PaddingValues(bottom = Large2X),
        verticalArrangement = Arrangement.spacedBy(Small),
    ) {
        grid.forEach { item ->
            item(span = { GridItemSpan(this.maxLineSpan) }) {
                Text(
                    modifier = Modifier.padding(top = Large2X, bottom = Normal),
                    text = item.title,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                )
            }

            items(count = item.padStart, key = { i -> "${item.title}_pad_$i" }) {
                Spacer(
                    Modifier.aspectRatio(1f)
                )
            }

            items(count = item.endDay, key = { i -> "${item.title}_day_$i" }) { day ->
                val selection = getSelectionForDate(
                    year = item.year,
                    month = item.month,
                    day = day + 1,
                    startSelection = startSelection,
                    endSelection = endSelection,
                )
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(vertical = Tinny)
                        .gridSelection(selection)
                        .clip(CircleShape)
                        .debounceClickable {
                            onDateClicked(item.year, item.month, day + 1)
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "${day + 1}",
                        textAlign = TextAlign.Center,
                        color = if (selection != null) MaterialTheme.colorScheme.onPrimary else Color.Unspecified,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun Modifier.gridSelection(selection: Int?): Modifier {
    return when (selection) {
        -1 -> this.background(
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(
                topStartPercent = 50,
                bottomStartPercent = 50,
            )
        )

        1 -> this.background(
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(
                topEndPercent = 50,
                bottomEndPercent = 50,
            )
        )

        0 -> this.background(color = MaterialTheme.colorScheme.primary)
        else -> this
    }
}

/**
 * -1 = start
 *  0 = middle
 *  1 = end
 *  null = none
 */
@Suppress("MagicNumber")
private fun getSelectionForDate(
    year: Int,
    month: Int,
    day: Int,
    startSelection: FiltersState.SelectionDate?,
    endSelection: FiltersState.SelectionDate?,
): Int? {
    if (startSelection == null) return null

    val current = year * 10_000 + month * 100 + day

    val start = startSelection.year * 10_000 + startSelection.month * 100 + startSelection.day

    if (current == start) return -1

    if (endSelection == null) return null

    val end = endSelection.year * 10_000 + endSelection.month * 100 + endSelection.day

    if (current == end) return 1

    @Suppress("ConvertTwoComparisonsToRangeCheck")
    return if (current > start && current < end) 0 else null
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun FormContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            FiltersContent(
                modifier = Modifier.fillMaxWidth(),
                state = FiltersState(
                    startSelection = FiltersState.SelectionDate(
                        year = 2025,
                        month = 11,
                        day = 14,
                    ),
                    endSelection = FiltersState.SelectionDate(
                        year = 2026,
                        month = 1,
                        day = 31,
                    ),
                    grid = persistentListOf(
                        FiltersState.GridItem(
                            title = "November 2025",
                            month = 11,
                            year = 2025,
                            endDay = 30,
                            padStart = 5,
                        ),
                        FiltersState.GridItem(
                            title = "December 2025",
                            month = 12,
                            year = 2025,
                            endDay = 31,
                            padStart = 0,
                        ),
                        FiltersState.GridItem(
                            title = "January 2026",
                            month = 1,
                            year = 2026,
                            endDay = 31,
                            padStart = 3,
                        )
                    )
                ),
                onDateClicked = { _, _, _ -> },
            )
        }
    }
}
