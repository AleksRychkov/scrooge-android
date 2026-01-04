package dev.aleksrychkov.scrooge.presentation.component.settingstheme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.entity.ThemeEntity
import dev.aleksrychkov.scrooge.presentation.component.settingstheme.internal.SettingsThemeComponentInternal
import dev.aleksrychkov.scrooge.presentation.component.settingstheme.internal.udf.SettingsThemeState
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun SettingsThemeContent(
    modifier: Modifier,
    component: SettingsThemeComponent,
) {
    SettingsThemeContent(
        modifier = modifier,
        component = component as SettingsThemeComponentInternal,
    )
}

@Composable
private fun SettingsThemeContent(
    modifier: Modifier,
    component: SettingsThemeComponentInternal,
) {
    val state by component.state.collectAsStateWithLifecycle()

    Content(
        modifier = modifier,
        state = state,
        setThemeType = component::setThemeType,
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    state: SettingsThemeState,
    setThemeType: (ThemeEntity.Type) -> Unit,
) {
    var isThemeDialogVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(horizontal = Large)
            .debounceClickable {
                isThemeDialogVisible = true
            }
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = CardDefaults.shape,
            )
            .padding(Normal),
    ) {
        Text(
            text = stringResource(Resources.string.theme),
            style = MaterialTheme.typography.titleMedium,
        )

        val current = when (state.theme.type) {
            ThemeEntity.Type.System -> stringResource(Resources.string.settings_select_theme_system)
            ThemeEntity.Type.Light -> stringResource(Resources.string.settings_select_theme_light)
            ThemeEntity.Type.Dark -> stringResource(Resources.string.settings_select_theme_dark)
            else -> ""
        }

        Text(
            modifier = Modifier.padding(top = Normal),
            text = current,
            style = MaterialTheme.typography.bodyMedium,
        )
    }

    if (isThemeDialogVisible) {
        ThemeModal(
            setThemeType = setThemeType,
            onDismiss = {
                isThemeDialogVisible = false
            }
        )
    }
}

@Composable
private fun ThemeModal(
    setThemeType: (ThemeEntity.Type) -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = CardDefaults.shape,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier
                        .padding(vertical = Normal, horizontal = Large),
                    text = stringResource(Resources.string.settings_select_theme),
                    style = MaterialTheme.typography.titleMedium,
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .debounceClickable {
                            onDismiss()
                            setThemeType(ThemeEntity.Type.System)
                        }
                        .padding(vertical = Normal, horizontal = Large),
                    text = stringResource(Resources.string.settings_select_theme_system)
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .debounceClickable {
                            onDismiss()
                            setThemeType(ThemeEntity.Type.Light)
                        }
                        .padding(vertical = Normal, horizontal = Large),
                    text = stringResource(Resources.string.settings_select_theme_light)
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .debounceClickable {
                            onDismiss()
                            setThemeType(ThemeEntity.Type.Dark)
                        }
                        .padding(vertical = Normal, horizontal = Large),
                    text = stringResource(Resources.string.settings_select_theme_dark)
                )
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
            Content(
                modifier = Modifier.fillMaxWidth(),
                state = SettingsThemeState(theme = ThemeEntity()),
                setThemeType = { _ -> },
            )
        }
    }
}
