package dev.aleksrychkov.scrooge.component.report.annualtotal

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.ReportAnnualTotalComponentInternal

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

@Suppress("UnusedParameter")
@Composable
private fun ReportAnnualTotalContent(
    modifier: Modifier,
    component: ReportAnnualTotalComponentInternal,
) {
    Column(
        modifier = modifier
    ) {
        Text("Annual total report")
    }
}
