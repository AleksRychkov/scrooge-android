package dev.aleksrychkov.scrooge.component.category.internal.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.component.category.internal.component.udf.CreateCategoryEffect
import dev.aleksrychkov.scrooge.component.category.internal.component.udf.CreateCategoryState
import dev.aleksrychkov.scrooge.core.designsystem.composables.AppButton
import dev.aleksrychkov.scrooge.core.designsystem.composables.CountdownSnackbar
import dev.aleksrychkov.scrooge.core.designsystem.composables.DialogSnackbarHost
import dev.aleksrychkov.scrooge.core.designsystem.composables.RoundedTextField
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.composables.showCountdownSnackbar
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.CategoryIconSize
import dev.aleksrychkov.scrooge.core.designsystem.theme.HalfNormal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Small
import dev.aleksrychkov.scrooge.core.resources.CategoryIcon
import dev.aleksrychkov.scrooge.core.resources.CategoryIcons
import dev.aleksrychkov.scrooge.core.resources.UncategorizedIcon
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
internal fun CreateCategoryContent(
    modifier: Modifier,
    component: CreateCategoryComponent,
    onCloseCallback: () -> Unit,
) {
    val state by component.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isDone) {
        if (state.isDone) onCloseCallback.invoke()
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    DisposableEffect(component) {
        val job = scope.launch {
            component.effects
                .onEach { effect ->
                    when (effect) {
                        is CreateCategoryEffect.ShowInfoMessage -> {
                            snackbarHostState.showCountdownSnackbar(message = effect.message)
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
        CreateCategoryContent(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            state = state,
            setName = component::setName,
            setIcon = component::setIcon,
            submit = component::submit,
        )
    }
}

@Composable
private fun CreateCategoryContent(
    modifier: Modifier,
    state: CreateCategoryState,
    setName: (String) -> Unit,
    setIcon: (CategoryIcon) -> Unit,
    submit: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(vertical = Normal, horizontal = Large)
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier
                    .height(CategoryIconSize)
                    .width(CategoryIconSize)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(Normal),
                tint = Color.White,
                imageVector = state.selectedCategoryIcon.icon,
                contentDescription = null,
            )

            RoundedTextField(
                modifier = Modifier
                    .weight(weight = 1f, fill = true)
                    .height(IntrinsicSize.Min)
                    .padding(horizontal = Normal),
                value = state.name,
                placeholder = {
                    if (state.name.isEmpty()) {
                        Text(
                            text = stringResource(Resources.string.category_name_placeholder),
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f),
                        )
                    }
                },
                onValueChanged = { value ->
                    setName(value)
                }
            )

            AppButton(
                modifier = Modifier.height(IntrinsicSize.Max),
                onClick = submit,
            ) {
                Text(text = stringResource(Resources.string.save))
            }
        }

        Text(
            modifier = Modifier.padding(vertical = HalfNormal),
            text = stringResource(Resources.string.category_select_icon),
            style = MaterialTheme.typography.bodyLarge,
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(CategoryIconSize),
            verticalArrangement = Arrangement.spacedBy(Small),
            horizontalArrangement = Arrangement.spacedBy(Small),
        ) {
            items(items = state.availableIcons) { item ->
                Box(
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .height(IntrinsicSize.Max),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        modifier = Modifier
                            .height(CategoryIconSize)
                            .width(CategoryIconSize)
                            .clip(CircleShape)
                            .debounceClickable {
                                setIcon(item)
                            }
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .padding(Normal),
                        tint = Color.White,
                        imageVector = item.icon,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun FormContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            CreateCategoryContent(
                modifier = Modifier.fillMaxWidth(),
                state = CreateCategoryState(
                    name = "Salary",
                    selectedCategoryIcon = UncategorizedIcon,
                    availableIcons = CategoryIcons.toImmutableList(),
                ),
                setName = {},
                setIcon = {},
                submit = {},
            )
        }
    }
}
