package dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.composables.CountdownSnackbar
import dev.aleksrychkov.scrooge.core.designsystem.composables.DialogSnackbarHost
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsButton
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsInputTextFieldsColors
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsTabBar
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.composables.showCountdownSnackbar
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.CategoryIconSize
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Small
import dev.aleksrychkov.scrooge.core.resources.CategoryIcon
import dev.aleksrychkov.scrooge.core.resources.CategoryIcons
import dev.aleksrychkov.scrooge.core.resources.UncategorizedIcon
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component.udf.CreateCategoryEffect
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component.udf.CreateCategoryState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
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
                            snackbarHostState.showCountdownSnackbar(
                                message = effect.message
                            )
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
            setColor = component::setColor,
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
    setColor: (Int) -> Unit,
    submit: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(horizontal = Large)
            .padding(bottom = Normal)
            .animateContentSize()
    ) {
        Header(
            modifier = Modifier.fillMaxWidth(),
            state = state,
            setName = setName,
            submit = submit,
        )

        var tabIndex by remember { mutableIntStateOf(0) }
        val titles = listOf(
            stringResource(Resources.string.category_select_icon),
            stringResource(Resources.string.category_select_color),
        )

        DsTabBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Large),
            options = titles,
            selectedIndex = tabIndex,
            onOptionSelected = { tabIndex = it }
        )

        AnimatedVisibility(
            visible = tabIndex == 0,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            SelectIcon(
                modifier = Modifier.fillMaxWidth(),
                state = state,
                setIcon = setIcon,
            )
        }
        AnimatedVisibility(
            visible = tabIndex == 1,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            SelectColor(
                modifier = Modifier.fillMaxWidth(),
                state = state,
                setColor = setColor,
            )
        }
    }
}

@Composable
private fun Header(
    modifier: Modifier,
    state: CreateCategoryState,
    setName: (String) -> Unit,
    submit: () -> Unit,
) {
    val isEditing = state.id != null
    val isLoading = state.isLoading
    val name = state.name
    val focusManager = LocalFocusManager.current

    Row(
        modifier = modifier.height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically,
    ) {
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
                    .background(Color(state.selectedCategoryColor))
                    .padding(Normal),
                tint = Color.White,
                imageVector = state.selectedCategoryIcon.icon,
                contentDescription = null,
            )
        }

        if (isEditing && isLoading) return@Row

        val categoryNameTextFieldState = rememberTextFieldState(initialText = "")
        LaunchedEffect(key1 = name) {
            if (name != categoryNameTextFieldState.text.toString()) {
                categoryNameTextFieldState.setTextAndPlaceCursorAtEnd(name)
            }
        }
        LaunchedEffect(categoryNameTextFieldState) {
            snapshotFlow { categoryNameTextFieldState.text.toString() }
                .collectLatest {
                    setName(it)
                }
        }

        TextField(
            modifier = Modifier
                .weight(weight = 1f, fill = true)
                .height(IntrinsicSize.Min)
                .padding(horizontal = Normal)
                .background(
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colorScheme.secondary,
                ),
            state = categoryNameTextFieldState,
            placeholder = {
                if (name.isEmpty()) {
                    Text(
                        text = stringResource(Resources.string.category_name_placeholder),
                        color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.25f),
                    )
                }
            },
            trailingIcon = {
                if (categoryNameTextFieldState.text.isNotBlank()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(Resources.string.clear),
                        modifier = Modifier
                            .clickable { categoryNameTextFieldState.setTextAndPlaceCursorAtEnd("") },
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text,
            ),
            onKeyboardAction = {
                focusManager.clearFocus()
            },
            colors = DsInputTextFieldsColors(),
        )

        DsButton(
            modifier = Modifier.fillMaxHeight(),
            onClick = submit,
        ) {
            Text(text = stringResource(Resources.string.save))
        }
    }
}

@Composable
private fun SelectIcon(
    modifier: Modifier,
    state: CreateCategoryState,
    setIcon: (CategoryIcon) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(CategoryIconSize),
        verticalArrangement = Arrangement.spacedBy(Small),
        horizontalArrangement = Arrangement.spacedBy(Small),
    ) {
        items(items = state.availableIcons, key = { it.id }) { item ->
            Box(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .height(IntrinsicSize.Max),
                contentAlignment = Alignment.Center,
            ) {
                val (tintColor, bgColor) = when {
                    item.id == state.selectedCategoryIcon.id -> Color.White to MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.onSecondary to MaterialTheme.colorScheme.secondary
                }
                Icon(
                    modifier = Modifier
                        .height(CategoryIconSize)
                        .width(CategoryIconSize)
                        .clip(CircleShape)
                        .debounceClickable {
                            setIcon(item)
                        }
                        .background(bgColor)
                        .padding(Normal),
                    tint = tintColor,
                    imageVector = item.icon,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
private fun SelectColor(
    modifier: Modifier,
    state: CreateCategoryState,
    setColor: (Int) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(CategoryIconSize),
        verticalArrangement = Arrangement.spacedBy(Small),
        horizontalArrangement = Arrangement.spacedBy(Small),
    ) {
        items(items = state.availableColors, key = { it.argb }) { color ->
            Box(
                modifier = Modifier
                    .height(CategoryIconSize)
                    .width(CategoryIconSize)
                    .clip(CircleShape)
                    .debounceClickable {
                        setColor(color.argb)
                    }
                    .background(color.color)
                    .padding(Normal),
            )
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
                    name = "",
                    selectedCategoryIcon = UncategorizedIcon,
                    availableIcons = CategoryIcons.toImmutableList(),
                ),
                setName = {},
                setIcon = {},
                setColor = {},
                submit = {},
            )
        }
    }
}
