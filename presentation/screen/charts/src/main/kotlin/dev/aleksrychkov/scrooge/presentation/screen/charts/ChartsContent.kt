package dev.aleksrychkov.scrooge.presentation.screen.charts

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsCardV2
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsFilterAction
import dev.aleksrychkov.scrooge.core.designsystem.composables.animateElevation
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppBarShadow
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.entity.readableName
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.BalanceTotalChartContent
import dev.aleksrychkov.scrooge.presentation.component.balancelinechart.IncomeExpenseChartContent
import dev.aleksrychkov.scrooge.presentation.component.categorylinechart.CategoryLineChartContent
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersBottomSheetModal
import dev.aleksrychkov.scrooge.presentation.screen.charts.internal.ChartsComponentInternal
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun ChartsContent(modifier: Modifier, component: ChartsComponent) {
    val internal = component as ChartsComponentInternal
    val state by internal.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    var openCategoryPicker by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        modifier = modifier,
        topBar = {
            ChartsAppBar(
                filterName = state.filter.readableName(),
                scrollState = scrollState,
                openFilters = internal::openFilters,
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState),
        ) {
            val currencySymbol = state.filter.currency?.currencySymbol.orEmpty()
            ChartCard(stringResource(R.string.balance_total_chart_title, currencySymbol)) {
                BalanceTotalChartContent(
                    modifier = Modifier.fillMaxWidth().height(CHART_HEIGHT),
                    component = internal.balanceTotalChart,
                )
            }
            ChartCard(stringResource(R.string.income_expense_chart_title, currencySymbol)) {
                IncomeExpenseChartContent(
                    modifier = Modifier.fillMaxWidth().height(CHART_HEIGHT),
                    component = internal.incomeExpenseChart,
                )
            }
            ChartCard(stringResource(R.string.category_chart_title, currencySymbol)) {
                CategoryLineChartContent(
                    modifier = Modifier.fillMaxWidth().height(CATEGORY_CHART_HEIGHT),
                    component = internal.categoryChart,
                    openCategoryFilter = {
                        openCategoryPicker = true
                        internal.openFilters()
                    },
                )
            }
        }
    }
    internal.filtersModal.subscribeAsState().value.child?.instance?.let { filters ->
        if (openCategoryPicker) {
            LaunchedEffect(filters) {
                filters.openCategoryModal()
                openCategoryPicker = false
            }
        }
        FiltersBottomSheetModal(
            component = filters,
            close = internal::closeFilters,
            setFilter = internal::setFilter,
        )
    }
}

@Composable
private fun dev.aleksrychkov.scrooge.core.entity.FilterEntity.readableName(): String = readableName(
    months = stringArrayResource(Resources.array.month_names),
    shortMonths = stringArrayResource(Resources.array.short_month_names),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChartsAppBar(
    filterName: String,
    scrollState: ScrollState,
    openFilters: () -> Unit,
) {
    val headerElevation by remember {
        derivedStateOf { if (scrollState.value > 0) AppBarShadow else 0.dp }
    }
    val animatedElevation by headerElevation.animateElevation()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = animatedElevation,
    ) {
        TopAppBar(
            title = { Text(stringResource(R.string.charts_title)) },
            actions = {
                DsFilterAction(
                    name = filterName,
                    showFilterIcon = true,
                    openFiltersModal = openFilters,
                )
            },
        )
    }
}

@Composable
private fun ChartCard(title: String, content: @Composable () -> Unit) {
    DsCardV2(
        modifier = Modifier.fillMaxWidth().padding(horizontal = Large, vertical = Large),
    ) {
        Column {
            Text(modifier = Modifier.padding(Large), text = title)
            content()
        }
    }
}

private val CHART_HEIGHT = 280.dp
private val CATEGORY_CHART_HEIGHT = 420.dp
