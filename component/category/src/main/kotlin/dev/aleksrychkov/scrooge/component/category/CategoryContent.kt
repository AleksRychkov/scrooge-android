package dev.aleksrychkov.scrooge.component.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.component.category.internal.CategoryComponentInternal
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryEffect
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryState
import dev.aleksrychkov.scrooge.core.designsystem.composables.CountdownSnackbar
import dev.aleksrychkov.scrooge.core.designsystem.composables.DialogSnackbarHost
import dev.aleksrychkov.scrooge.core.designsystem.composables.NavigationBarSpacer
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.composables.showCountdownSnackbar
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun CategoryContent(
    modifier: Modifier,
    component: CategoryComponent,
    callback: (CategoryEntity?) -> Unit,
) {
    CategoryContent(
        modifier = modifier,
        component = component as CategoryComponentInternal,
        callback = callback,
    )
}

@Composable
private fun CategoryContent(
    modifier: Modifier,
    component: CategoryComponentInternal,
    callback: (CategoryEntity?) -> Unit,
) {
    val state by component.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }
    DisposableEffect(component) {
        val job = scope.launch {
            component.effects
                .onEach { effect ->
                    when (effect) {
                        is CategoryEffect.ShowInfoMessage -> {
                            snackbarHostState.showCountdownSnackbar(message = effect.message)
                        }

                        is CategoryEffect.CategoryDeleted -> {
                            val result = snackbarHostState.showCountdownSnackbar(
                                message = effect.message,
                                actionLabel = effect.actionLabel,
                                useCountDown = true,
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                component.restoreCategory(category = effect.category)
                            }
                        }
                    }
                }
                .collect()
        }
        onDispose {
            job.cancel()
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        snackbarHost = {
            DialogSnackbarHost(
                snackbarHostState = snackbarHostState,
                snackbar = { data ->
                    CountdownSnackbar(data)
                },
            )
        },
    ) { innerPadding ->
        CategoryContent(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            state = state,
            selectCategory = callback,
            deleteCategory = component::deleteCategory,
            setSearchQuery = component::setSearchQuery,
            addNewCategoryClicked = component::addNewCategory,
        )
    }
}

@Composable
private fun CategoryContent(
    modifier: Modifier,
    state: CategoryState,
    selectCategory: (CategoryEntity) -> Unit,
    deleteCategory: (CategoryEntity) -> Unit,
    setSearchQuery: (String) -> Unit,
    addNewCategoryClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .displayCutoutPadding()
            .statusBarsPadding()
    ) {
        CategoryBar(
            modifier = Modifier.fillMaxWidth(),
            state = state,
            setSearchQuery = setSearchQuery,
            addCategoryClicked = addNewCategoryClicked,
        )

        Spacer(modifier = Modifier.height(Normal))

        CategoryList(
            modifier = Modifier.fillMaxSize(),
            state = state,
            selectCategory = selectCategory,
            deleteCategory = deleteCategory,
        )
    }
}

@Composable
private fun CategoryList(
    modifier: Modifier,
    state: CategoryState,
    selectCategory: (CategoryEntity) -> Unit,
    deleteCategory: (CategoryEntity) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        val categories = if (state.searchQuery.isNotBlank()) {
            state.filtered
        } else {
            state.categories
        }

        items(
            items = categories,
            key = { category -> category.id }
        ) { category ->
            Category(
                modifier = Modifier.animateItem(),
                value = category,
                selectCategory = selectCategory,
                deleteCategory = deleteCategory,
            )
        }

        item {
            NavigationBarSpacer()
        }
    }
}

@Composable
private fun Category(
    modifier: Modifier = Modifier,
    value: CategoryEntity,
    selectCategory: (CategoryEntity) -> Unit,
    deleteCategory: (CategoryEntity) -> Unit,
) {
    val itemHeight = 60.dp
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = itemHeight)
            .clickable {
                selectCategory(value)
            }
            .padding(start = Large),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .weight(weight = 1f, fill = true),
            text = value.name,
        )

        if (value.isUserMade) {
            Box(
                modifier = Modifier
                    .height(itemHeight)
                    .aspectRatio(1f)
                    .debounceClickable {
                        deleteCategory(value)
                    },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(Resources.string.category_delete),
                )
            }
        }
    }
}

@Composable
private fun CategoryBar(
    modifier: Modifier,
    state: CategoryState,
    setSearchQuery: (String) -> Unit,
    addCategoryClicked: () -> Unit,
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .padding(start = Normal)
                .weight(weight = 1f, fill = true),
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.searchQuery,
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                trailingIcon = {
                    if (state.searchQuery.isNotBlank()) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(Resources.string.clear),
                            modifier = Modifier
                                .clickable { setSearchQuery("") },
                        )
                    }
                },
                onValueChange = setSearchQuery,
            )
        }

        Box(
            modifier = Modifier
                .height(60.dp)
                .aspectRatio(1f)
                .debounceClickable(onClick = addCategoryClicked),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = stringResource(Resources.string.category_add),
            )
        }
    }
}
