package dev.aleksrychkov.scrooge.presentation.screen.report.categorytotal

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsFilterAction
import dev.aleksrychkov.scrooge.core.entity.readableName
import dev.aleksrychkov.scrooge.presentation.screen.report.categorytotal.internal.ReportCategoryTotalComponentInternal
import dev.aleksrychkov.scrooge.presentation.screen.report.categorytotal.internal.component.bycategory.ByCategoryContent
import dev.aleksrychkov.scrooge.presentation.screen.report.categorytotal.internal.modal.FiltersModal
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun ReportCategoryTotalContent(
    modifier: Modifier,
    component: ReportCategoryTotalComponent,
) {
    ReportCategoryTotalContent(
        modifier = modifier,
        component = component as ReportCategoryTotalComponentInternal,
    )
}

@Composable
private fun ReportCategoryTotalContent(
    modifier: Modifier,
    component: ReportCategoryTotalComponentInternal,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            ReportAppBar(
                component = component,
            )
        }
    ) { innerPadding ->
        Content(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            component = component,
        )
    }
    FiltersModal(
        component = component,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReportAppBar(
    component: ReportCategoryTotalComponentInternal,
) {
    val state by component.state.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier.fillMaxWidth(),
    ) {
        TopAppBar(
            title = {
                Text(text = stringResource(Resources.string.categories))
            },
            navigationIcon = {
                IconButton(onClick = component::onBackClicked) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Resources.string.back),
                    )
                }
            },
            actions = {
                val months = stringArrayResource(Resources.array.month_names)
                val shortMonths = stringArrayResource(Resources.array.short_month_names)
                val name = state.filter.readableName(months = months, shortMonths = shortMonths)
                val showFilterIcon = with(state.filter) {
                    tags.isNotEmpty() || category != null || transactionType != null
                }
                DsFilterAction(
                    name = name,
                    showFilterIcon = showFilterIcon,
                    openFiltersModal = component::openFiltersModal,
                )
            }
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    component: ReportCategoryTotalComponentInternal,
) {
    ByCategoryContent(
        modifier = modifier,
        component = component.byCategoryComponent,
    )
}
