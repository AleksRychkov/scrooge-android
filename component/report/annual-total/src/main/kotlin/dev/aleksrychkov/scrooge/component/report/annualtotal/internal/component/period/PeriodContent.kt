package dev.aleksrychkov.scrooge.component.report.annualtotal.internal.component.period

import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
internal fun PeriodContent(
    modifier: Modifier,
    component: PeriodComponent,
    callback: (Int) -> Unit,
) {
    val state = remember { component.state }

    PeriodContent(
        modifier = modifier,
        state = state,
        onYearClicked = { year ->
            callback(year)
        },
        onCurrentYearClicked = {
            callback(state.currentYear)
        }
    )
}

@Composable
private fun PeriodContent(
    modifier: Modifier,
    state: PeriodState,
    onYearClicked: (Int) -> Unit,
    onCurrentYearClicked: () -> Unit,
) {
    Column(modifier = modifier) {
        val selectedItemIndex = state.allYears.indexOf(state.selectedYear)
        val listState = rememberLazyListState(
            initialFirstVisibleItemIndex = selectedItemIndex,
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            state = listState,
            contentPadding = PaddingValues(
                start = Large,
                end = Large,
            ),
            horizontalArrangement = Arrangement.spacedBy(Normal),
        ) {
            items(
                items = state.allYears,
                key = { it }
            ) { year ->
                OutlinedButton(
                    modifier = Modifier.padding(),
                    onClick = {
                        onYearClicked(year)
                    }
                ) {
                    val color = if (year == state.selectedYear) {
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

        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Normal, horizontal = Large),
            shape = MaterialTheme.shapes.large,
            onClick = onCurrentYearClicked,
        ) {
            Text(
                color = MaterialTheme.colorScheme.onSecondary,
                text = stringResource(Resources.string.reports_period_current_year)
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
