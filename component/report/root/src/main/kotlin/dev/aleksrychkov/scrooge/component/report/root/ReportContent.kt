package dev.aleksrychkov.scrooge.component.report.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import dev.aleksrychkov.scrooge.component.report.annualtotal.ReportAnnualTotalContent
import dev.aleksrychkov.scrooge.component.report.root.internal.ReportComponentInternal

@Composable
fun ReportContent(
    modifier: Modifier,
    component: ReportComponent
) {
    Content(
        modifier = modifier,
        component = component as ReportComponentInternal,
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    component: ReportComponentInternal,
) {
    Children(
        modifier = modifier,
        stack = component.stack,
    ) {
        when (val child = it.instance) {
            is ReportComponentInternal.Child.Annual -> {
                ReportAnnualTotalContent(
                    modifier = Modifier.fillMaxSize(),
                    component = child.component,
                )
            }
        }
    }
}
