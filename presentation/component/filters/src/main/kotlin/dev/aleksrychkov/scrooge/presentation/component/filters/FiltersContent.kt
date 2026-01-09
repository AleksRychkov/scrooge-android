@file:Suppress("All")

package dev.aleksrychkov.scrooge.presentation.component.filters

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsButton
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.FiltersComponentInternal
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.composables.FiltersFixedPeriod
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.modal.FiltersCategoryModal
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.modal.FiltersTagModal
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersEffect
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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
        onYearLongClicked = component::onYearLongClicked,
        onMonthClicked = component::onMonthClicked,
        onMonthLongClicked = component::onMonthLongClicked,
        openTagModal = component::openTagModal,
        removeTag = component::removeTag,
        openCategoryModal = component::openCategoryModal,
        removeCategory = component::removeCategory,
        onSubmitClicked = { callback(state.filter) },
        resetFilters = component::resetFilters,
    )

    HandleEffects(
        component = component,
        callback = callback,
    )

    FiltersTagModal(
        component = component,
    )
    FiltersCategoryModal(
        component = component,
    )
}

@Composable
private fun FiltersContent(
    modifier: Modifier,
    state: FiltersState,
    onYearClicked: (Int) -> Unit,
    onYearLongClicked: (Int) -> Unit,
    onMonthClicked: (Int) -> Unit,
    onMonthLongClicked: (Int) -> Unit,
    onSubmitClicked: () -> Unit,
    openTagModal: () -> Unit,
    removeTag: (TagEntity) -> Unit,
    openCategoryModal: () -> Unit,
    removeCategory: () -> Unit,
    resetFilters: () -> Unit,
) {
    Box(modifier = modifier.verticalScroll(rememberScrollState())) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

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
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                settings = state.settings,
                allYears = state.allYears,
                allMonths = state.allMonths,
                selectedYears = state.filter.years,
                selectedMonths = state.filter.months,
                selectedTags = state.filter.tags,
                category = state.filter.category,
                onYearClicked = onYearClicked,
                onYearLongClicked = onYearLongClicked,
                onMonthClicked = onMonthClicked,
                onMonthLongClicked = onMonthLongClicked,
                removeTag = removeTag,
                openTagModal = openTagModal,
                openCategoryModal = openCategoryModal,
                removeCategory = removeCategory,
            )
        }

        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(Large),
            onClick = resetFilters,
        ) {
            Text(text = stringResource(Resources.string.reset))
        }
    }
}

@Composable
private fun HandleEffects(
    component: FiltersComponentInternal,
    callback: (FilterEntity) -> Unit,
) {
    val scope = rememberCoroutineScope()
    DisposableEffect(component) {
        val job = scope.launch {
            component.effects
                .onEach { effect ->
                    when (effect) {
                        is FiltersEffect.SubmitFilters -> {
                            callback(effect.filter)
                        }
                    }
                }
                .collect()
        }
        onDispose {
            job.cancel()
        }
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
                onYearLongClicked = { _ -> },
                onMonthClicked = { _ -> },
                onMonthLongClicked = { _ -> },
                onSubmitClicked = {},
                removeTag = { _ -> },
                openTagModal = {},
                openCategoryModal = {},
                removeCategory = {},
                resetFilters = {},
            )
        }
    }
}
