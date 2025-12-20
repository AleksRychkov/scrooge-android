package dev.aleksrychkov.scrooge.component.report.categorytotal

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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.aleksrychkov.scrooge.component.report.categorytotal.internal.ReportCategoryTotalComponentInternal
import dev.aleksrychkov.scrooge.component.report.categorytotal.internal.component.bycategory.ByCategoryContent
import dev.aleksrychkov.scrooge.core.designsystem.theme.Medium
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
                onBackClicked = {
                    throw IllegalStateException("Not implemented yet")
                }
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReportAppBar(
    component: ReportCategoryTotalComponentInternal,
    onBackClicked: () -> Unit,
) {
    val state = remember { component.periodComponent.state }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = Medium,
    ) {
        TopAppBar(
            title = {
                Text(text = stringResource(Resources.string.category))
            },
            navigationIcon = {
                IconButton(onClick = onBackClicked) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Resources.string.back),
                    )
                }
            },
            actions = {
                TextButton(
                    onClick = component::openPeriodModal,
                ) {
                    Text(text = state.name)
                }
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
