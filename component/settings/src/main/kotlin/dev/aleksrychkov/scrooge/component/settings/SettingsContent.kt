package dev.aleksrychkov.scrooge.component.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.aleksrychkov.scrooge.component.settings.internal.SettingsComponentInternal

@Composable
fun SettingsContent(
    modifier: Modifier,
    component: SettingsComponent
) {
    SettingsContent(
        modifier = modifier,
        component = component as SettingsComponentInternal,
    )
}

@Suppress("unused")
@Composable
private fun SettingsContent(
    modifier: Modifier,
    component: SettingsComponentInternal
) {
    Column(
        modifier = modifier
            .displayCutoutPadding()
            .statusBarsPadding()
    ) {
        Text("Settings")
    }
}
