package dev.aleksrychkov.scrooge.presentation.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.presentation.component.settingstheme.SettingsThemeComponent
import dev.aleksrychkov.scrooge.presentation.component.settingstheme.SettingsThemeContent
import dev.aleksrychkov.scrooge.presentation.component.settingstransfer.export.TransferExportComponent
import dev.aleksrychkov.scrooge.presentation.component.settingstransfer.export.TransferExportContent
import dev.aleksrychkov.scrooge.presentation.component.settingstransfer.import.TransferImportComponent
import dev.aleksrychkov.scrooge.presentation.component.settingstransfer.import.TransferImportContent
import dev.aleksrychkov.scrooge.presentation.screen.settings.internal.SettingsComponentInternal
import dev.aleksrychkov.scrooge.core.resources.R as Resources

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
    Scaffold(
        modifier = modifier,
        topBar = {
            SettingsAppBar()
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
private fun SettingsAppBar() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
    ) {
        TopAppBar(
            title = {
                Text(text = stringResource(Resources.string.settings))
            }
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    component: SettingsComponentInternal,
) {
    Column(
        modifier = modifier
    ) {
        Theme(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Large)
                .padding(horizontal = Large),
            component = component.settingsThemeComponent,
        )

        Import(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Normal)
                .padding(horizontal = Large),
            component = component.transferImportComponent,
        )

        Export(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Normal)
                .padding(horizontal = Large),
            component = component.transferExportComponent,
        )
    }
}

@Composable
private fun Theme(
    modifier: Modifier,
    component: SettingsThemeComponent
) {
    SettingsThemeContent(
        modifier = modifier,
        component = component,
    )
}

@Composable
private fun Import(
    modifier: Modifier,
    component: TransferImportComponent,
) {
    TransferImportContent(
        modifier = modifier,
        component = component,
    )
}

@Composable
private fun Export(
    modifier: Modifier,
    component: TransferExportComponent,
) {
    TransferExportContent(
        modifier = modifier,
        component = component,
    )
}
