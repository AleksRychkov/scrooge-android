package dev.aleksrychkov.scrooge.component.report.annualtotal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.ReportAnnualTotalComponentInternal
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.composables.SelectYear
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf.ReportAnnualTotalState

@Composable
fun ReportAnnualTotalContent(
    modifier: Modifier,
    component: ReportAnnualTotalComponent,
) {
    ReportAnnualTotalContent(
        modifier = modifier,
        component = component as ReportAnnualTotalComponentInternal,
    )
}

@Composable
private fun ReportAnnualTotalContent(
    modifier: Modifier,
    component: ReportAnnualTotalComponentInternal,
) {
    val state by component.state.collectAsStateWithLifecycle()

    ReportAnnualTotalContent(
        modifier = modifier,
        state = state,
        onIncrementClicked = component::incrementYear,
        onDecrementClicked = component::decrementYear,
        onCurrentYearClicked = component::currentYear,
    )
}

@Composable
private fun ReportAnnualTotalContent(
    modifier: Modifier,
    state: ReportAnnualTotalState,
    onIncrementClicked: () -> Unit,
    onDecrementClicked: () -> Unit,
    onCurrentYearClicked: () -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        SelectYear(
            modifier = Modifier.fillMaxWidth(),
            year = state.selectedYear,
            onIncrementClicked = onIncrementClicked,
            onDecrementClicked = onDecrementClicked,
            onCurrentYearClicked = onCurrentYearClicked,
        )
    }
}
