@file:Suppress("All")

package dev.aleksrychkov.scrooge.component.report.categorytotal.internal.component.bycategory

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
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
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
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
    if (byCurrency.isEmpty()) return
    BoxWithConstraints(modifier = modifier) {
        val pagerState = rememberPagerState(
            pageCount = { byCurrency.size }
        )
        val maxBottomSheetOffset = with(LocalDensity.current) { maxWidth.toPx() * 0.67f }
        val bottomSheetOffset = remember { Animatable(maxBottomSheetOffset) }

        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            state = pagerState,
            beyondViewportPageCount = 1,
            verticalAlignment = Alignment.Top,
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                pagerSnapDistance = PagerSnapDistance.atMost(0)
            )
        ) { page ->
            ByCategoryChart(
                modifier = Modifier.fillMaxWidth(),
                page = page,
                pagerState = pagerState,
                maxWidth = this@BoxWithConstraints.maxWidth,
                chartData = byCurrency[page].chartData,
                currencySymbol = byCurrency[page].currencySymbol,
                bottomSheetOffset = bottomSheetOffset,
                maxBottomSheetOffset = maxBottomSheetOffset,
            )
        }

        ByCategoryBottomSheet(
            modifier = Modifier.fillMaxSize(),
            data = byCurrency[pagerState.currentPage].valueData,
            maxOffset = maxBottomSheetOffset,
            sheetOffset = bottomSheetOffset,
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ByCategoryBottomSheet(
    modifier: Modifier,
    data: List<ByCategoryState.ByCurrency.Value>,
    maxOffset: Float,
    sheetOffset: Animatable<Float, AnimationVector1D>,
) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y
                val scrollingUp = delta < 0
                val scrollingDown = delta > 0

                val listCanScrollUp = listState.firstVisibleItemIndex > 0 ||
                        listState.firstVisibleItemScrollOffset > 0
                val sheetExpanded = sheetOffset.value == 0f

                val shouldScrollSheet = when {
                    scrollingUp -> !sheetExpanded
                    scrollingDown -> !listCanScrollUp
                    else -> false
                }

                if (shouldScrollSheet) {
                    val newOffset = (sheetOffset.value + delta).coerceIn(0f, maxOffset)
                    if (newOffset != sheetOffset.value) {
                        scope.launch { sheetOffset.snapTo(newOffset) }
                        return Offset(0f, delta)
                    }
                }
                return Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                val target = when {
                    available.y < -1000f -> 0f
                    available.y > 1000f -> maxOffset
                    sheetOffset.value < maxOffset / 2f -> 0f
                    else -> maxOffset
                }

                sheetOffset.animateTo(
                    targetValue = target,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessLow,
                    )
                )
                return Velocity.Zero
            }
        }
    }

    Column(
        modifier = modifier
            .offset { IntOffset(0, sheetOffset.value.roundToInt()) }
            .background(MaterialTheme.colorScheme.background)
            .nestedScroll(nestedScrollConnection)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Normal),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(width = 32.dp, height = 4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        shape = MaterialTheme.shapes.extraLarge,
                    )
            )
        }

        LaunchedEffect(data) {
            listState.scrollToItem(0)
        }

        AnimatedContent(
            targetState = data,
            transitionSpec = {
                fadeIn(tween(durationMillis = 500))
                    .togetherWith(fadeOut(tween(durationMillis = 500)))
            },
            label = "ByCategoryListFade"
        ) { animatedData ->

            ByCategoryList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Large),
                data = animatedData,
                listState = listState,
            )
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
    bottomSheetOffset: Animatable<Float, AnimationVector1D>,
    maxBottomSheetOffset: Float,
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
                this.alpha = lerp(1f, 0f, 1f - bottomSheetOffset.value / maxBottomSheetOffset)
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
            segments = chartData,
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
    listState: LazyListState,
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
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
