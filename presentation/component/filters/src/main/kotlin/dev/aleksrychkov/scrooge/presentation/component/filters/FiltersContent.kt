@file:Suppress("All")

package dev.aleksrychkov.scrooge.presentation.component.filters

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsButton
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.FiltersComponentInternal
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.composables.FiltersFixedPeriod
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Month
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun FiltersContent(
    modifier: Modifier,
    component: FiltersComponent,
    callback: (FilterEntity) -> Unit,
) {
    FiltersContent(
        modifier = modifier,
        component = component as FiltersComponentInternal,
        callback = callback,
    )
}

@Composable
private fun FiltersContent(
    modifier: Modifier,
    component: FiltersComponentInternal,
    callback: (FilterEntity) -> Unit,
) {
    val state by component.state.collectAsStateWithLifecycle()

    FiltersContent(
        modifier = modifier,
        state = state,
        onYearClicked = component::onYearClicked,
        onMonthClicked = component::onMonthClicked,
        onSubmitClicked = {
            callback(state.filter)
        },
    )
}

@Composable
private fun FiltersContent(
    modifier: Modifier,
    state: FiltersState,
    onYearClicked: (Int) -> Unit,
    onMonthClicked: (Int) -> Unit,
    onSubmitClicked: () -> Unit,
) {
    Column(modifier = modifier) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Large),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = state.filterReadable,
                style = MaterialTheme.typography.bodyMedium,
            )

            DsButton(
                onClick = onSubmitClicked
            ) {
                Text(stringResource(Resources.string.apply))
            }
        }

        FiltersFixedPeriod(
            modifier = Modifier.fillMaxSize(),
            allYears = state.allYears,
            allMonths = state.allMonths,
            selectedYear = state.selectedYear,
            selectedMonth = state.selectedMonthNumber,
            onYearClicked = onYearClicked,
            onMonthClicked = onMonthClicked,
        )
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun FormContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            FiltersContent(
                modifier = Modifier.fillMaxSize(),
                state = FiltersState(
                    allYears = persistentListOf(2024),
                    allMonths = Month.entries.map { it.name }.toImmutableList(),
                ),
                onYearClicked = { _ -> },
                onMonthClicked = { _ -> },
                onSubmitClicked = {},
            )
        }
    }
}
