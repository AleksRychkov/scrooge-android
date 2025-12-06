package dev.aleksrychkov.scrooge.component.report.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.arkivanov.decompose.extensions.compose.stack.Children
import dev.aleksrychkov.scrooge.component.report.root.internal.ReportComponentInternal
import dev.aleksrychkov.scrooge.core.resources.R as Resources

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
    Scaffold(
        modifier = modifier,
        topBar = {
            ReportAppBar()
        }
    ) { innerPadding ->
        Content(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            component = component,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReportAppBar() {
    TopAppBar(
        title = {
            Text(text = stringResource(Resources.string.reports))
        }
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
    ) { }
}
