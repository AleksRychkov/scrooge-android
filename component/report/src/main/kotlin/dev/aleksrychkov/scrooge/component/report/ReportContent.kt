package dev.aleksrychkov.scrooge.component.report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.aleksrychkov.scrooge.component.report.internal.ReportComponentInternal

@Composable
fun ReportContent(
    modifier: Modifier,
    component: ReportComponent
) {
    ReportContent(
        modifier = modifier,
        component = component as ReportComponentInternal,
    )
}

@Suppress("unused")
@Composable
private fun ReportContent(
    modifier: Modifier,
    component: ReportComponentInternal
) {
    Column(
        modifier = modifier
            .displayCutoutPadding()
            .statusBarsPadding()
    ) {
        Text("Report")
    }
}
