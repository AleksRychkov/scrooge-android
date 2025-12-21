@file:Suppress("All")

package dev.aleksrychkov.scrooge.component.report.categorytotal.internal.component.bycategory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.component.report.categorytotal.internal.component.bycategory.udf.ByCategoryState
import dev.aleksrychkov.scrooge.component.report.categorytotal.internal.composables.PieChart
import dev.aleksrychkov.scrooge.component.report.categorytotal.internal.composables.PieChartSegment
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsTabBar
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Medium
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.math.absoluteValue
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
internal fun ByCategoryContent(
    modifier: Modifier,
    component: ByCategoryComponent,
) {
    val state by component.state.collectAsStateWithLifecycle()

    ByCategoryContent(
        modifier = modifier,
        state = state,
        setType = component::setTransactionType,
    )
}

@Composable
private fun ByCategoryContent(
    modifier: Modifier,
    state: ByCategoryState,
    setType: (Int) -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        var tabIndex by rememberSaveable {
            mutableIntStateOf(TransactionType.Expense.type)
        }
        val titles = listOf(
            stringResource(Resources.string.income),
            stringResource(Resources.string.expense),
        )
        DsTabBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Large),
            options = titles,
            selectedIndex = tabIndex,
            onOptionSelected = {
                tabIndex = it
                setType(it)
            }
        )

        val byCurrency = if (state.currentType == TransactionType.Expense) {
            state.byCurrencyExpense
        } else {
            state.byCurrencyIncome
        }
        ByCurrency(
            modifier = Modifier
                .fillMaxWidth(),
            byCurrency = byCurrency,
        )
    }
}

@Composable
private fun ByCurrency(
    modifier: Modifier,
    byCurrency: ImmutableList<ByCategoryState.ByCurrency>,
) {
    BoxWithConstraints(modifier = modifier) {
        val pagerState = rememberPagerState(
            pageCount = { byCurrency.size }
        )
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            contentPadding = PaddingValues(horizontal = Large),
            pageSpacing = Large,
            beyondViewportPageCount = 1,
            verticalAlignment = Alignment.Top,
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                pagerSnapDistance = PagerSnapDistance.atMost(0)
            )
        ) { page ->
            Column(modifier = Modifier.fillMaxWidth()) {
                ByCategoryChart(
                    modifier = Modifier.fillMaxWidth(),
                    page = page,
                    pagerState = pagerState,
                    maxWidth = this@BoxWithConstraints.maxWidth,
                    chartData = byCurrency[page].chartData,
                    currencySymbol = byCurrency[page].currencySymbol,
                )
                ByCategoryList(
                    modifier = Modifier.fillMaxWidth(),
                    data = byCurrency[page].valueData
                )
            }
        }
    }
}

@Composable
private fun ByCategoryChart(
    modifier: Modifier,
    page: Int,
    pagerState: PagerState,
    maxWidth: Dp,
    chartData: ImmutableList<PieChartSegment>,
    currencySymbol: String,
) {
    val rawPageOffset =
        (pagerState.currentPage - page) +
                pagerState.currentPageOffsetFraction

    val chartWidthMultiplier = 0.65f
    Box(
        modifier = modifier
            .graphicsLayer {
                val chartWidthPx = size.width * chartWidthMultiplier
                translationX = rawPageOffset * (chartWidthPx / 1.5f)
            },
        contentAlignment = Alignment.Center,
    ) {
        PieChart(
            modifier = Modifier
                .width(maxWidth * chartWidthMultiplier)
                .aspectRatio(1f)
                .graphicsLayer {
                    val pageOffSet = (
                            (pagerState.currentPage - page) + pagerState
                                .currentPageOffsetFraction
                            ).absoluteValue
                    scaleY = lerp(
                        start = 0.7f,
                        stop = 1f,
                        fraction = 1f - pageOffSet.coerceIn(0f, 1f)
                    )
                    scaleX = lerp(
                        start = 0.7f,
                        stop = 1f,
                        fraction = 1f - pageOffSet.coerceIn(0f, 1f)
                    )
                },
            segments = if (pagerState.currentPage == page) chartData else persistentListOf(),
            animateOnSegmentChange = true,
        )
        Text(
            text = currencySymbol,
            style = MaterialTheme.typography.titleLarge,
        )
    }

}

@Composable
private fun ByCategoryList(
    modifier: Modifier,
    data: List<ByCategoryState.ByCurrency.Value>,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(
            items = data,
            key = { it.categoryName }
        ) { value ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Normal),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier
                        .height(36.dp)
                        .width(36.dp)
                        .clip(CircleShape)
                        .background(Color(value.categoryColor))
                        .padding(Medium),
                    tint = Color.White,
                    imageVector = value.categoryIcon.icon,
                    contentDescription = null,
                )

                Text(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .padding(horizontal = Normal),
                    text = value.categoryName,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                Text(
                    color = Color.Unspecified,
                    text = "${value.amount} ${value.currencySymbol}",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}
