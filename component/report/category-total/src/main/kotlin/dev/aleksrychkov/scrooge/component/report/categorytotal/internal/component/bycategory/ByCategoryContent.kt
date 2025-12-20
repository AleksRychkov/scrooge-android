@file:Suppress("All")
package dev.aleksrychkov.scrooge.component.report.categorytotal.internal.component.bycategory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.component.report.categorytotal.internal.component.bycategory.udf.ByCategoryState
import dev.aleksrychkov.scrooge.component.report.categorytotal.internal.composables.DonutChart
import dev.aleksrychkov.scrooge.component.report.categorytotal.internal.composables.DonutChartSegment
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsTabBar
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
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
    )
}

@Composable
private fun ByCategoryContent(
    modifier: Modifier,
    state: ByCategoryState,
) {
    Column(
        modifier = modifier
    ) {
        var tabIndex by remember {
            mutableIntStateOf(1)
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
            onOptionSelected = { tabIndex = it }
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
            modifier = Modifier.fillMaxWidth(),
            state = pagerState,
            contentPadding = PaddingValues(horizontal = Large),
            pageSpacing = Large,
            beyondViewportPageCount = 1,
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                pagerSnapDistance = PagerSnapDistance.atMost(0)
            )
        ) { page ->

            val rawPageOffset =
                (pagerState.currentPage - page) +
                    pagerState.currentPageOffsetFraction

            val chartWidthMultiplier = 0.7f
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Large)
                        .graphicsLayer {
                            val chartWidthPx = size.width * chartWidthMultiplier
                            translationX = rawPageOffset * (chartWidthPx / 2f)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    DonutChart(
                        modifier = Modifier
                            .width(this@BoxWithConstraints.maxWidth * chartWidthMultiplier)
                            .aspectRatio(1f)
                            .graphicsLayer {
                                val pageOffSet = (
                                    (pagerState.currentPage - page) + pagerState
                                        .currentPageOffsetFraction
                                    ).absoluteValue
                                scaleY = lerp(
                                    start = 0.75f,
                                    stop = 1f,
                                    fraction = 1f - pageOffSet.coerceIn(0f, 1f)
                                )
                                scaleX = lerp(
                                    start = 0.75f,
                                    stop = 1f,
                                    fraction = 1f - pageOffSet.coerceIn(0f, 1f)
                                )
                            },
                        segments = byCurrency[page].chartData,
                        animateOnSegmentChange = true,
                        strokeWidth = 32.dp,
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color.Red)
                )
            }
        }
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun ByCategoryContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            ByCategoryContent(
                modifier = Modifier.fillMaxWidth(),
                state = ByCategoryState(
                    isLoading = false,
                    byCurrencyExpense = persistentListOf(
                        ByCategoryState.ByCurrency(
                            currency = CurrencyEntity.RUB,
                            chartData = persistentListOf(
                                DonutChartSegment(0.67f, Color(0xFFFFB54B)),
                                DonutChartSegment(0.18f, Color(0xFFE53935)),
                                DonutChartSegment(0.05f, Color(0xFF5E35B1)),
                                DonutChartSegment(0.02f, Color(0xFFFFA48C)),
                                DonutChartSegment(0.02f, Color(0xFFC0CA33)),
                                DonutChartSegment(0.02f, Color(0xFF3949AB)),
                                DonutChartSegment(0.02f, Color(0xFF00ACC1)),
                                DonutChartSegment(0.01f, Color(0xFFFFB300)),
                                DonutChartSegment(0.01f, Color(0xFF00897B)),
                            ),
                            valueData = persistentListOf(),
                        ),
                        ByCategoryState.ByCurrency(
                            currency = CurrencyEntity.EUR,
                            chartData = persistentListOf(
                                DonutChartSegment(0.67f, Color(0xFFFFB54B)),
                                DonutChartSegment(0.18f, Color(0xFFE53935)),
                                DonutChartSegment(0.05f, Color(0xFF5E35B1)),
                                DonutChartSegment(0.02f, Color(0xFFFFA48C)),
                                DonutChartSegment(0.02f, Color(0xFFC0CA33)),
                                DonutChartSegment(0.02f, Color(0xFF3949AB)),
                                DonutChartSegment(0.02f, Color(0xFF00ACC1)),
                                DonutChartSegment(0.01f, Color(0xFFFFB300)),
                                DonutChartSegment(0.01f, Color(0xFF00897B)),
                            ),
                            valueData = persistentListOf(),
                        )
                    )
                ),
            )
        }
    }
}
