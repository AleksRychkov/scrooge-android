package dev.aleksrychkov.scrooge.presentation.screen.settings

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import dev.aleksrychkov.scrooge.core.designsystem.composables.animateElevation
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppBarShadow
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.presentation.component.category.CategoryModal
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
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = modifier,
        topBar = {
            SettingsAppBar(scrollState = scrollState)
        }
    ) { innerPadding ->
        Content(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            component = component,
            scrollState = scrollState,
        )
    }
    CategoryModal(
        slot = component.categoryModal.subscribeAsState().value,
        close = component::closeCategories,
        select = {},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsAppBar(scrollState: ScrollState) {
    val headerElevation by remember {
        derivedStateOf { if (scrollState.value > 0) AppBarShadow else 0.dp }
    }
    val animatedElevation by headerElevation.animateElevation()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = animatedElevation,
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
    scrollState: ScrollState,
) {
    Column(
        modifier = modifier.verticalScroll(scrollState),
    ) {
        Theme(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Large)
                .padding(horizontal = Large),
            component = component.settingsThemeComponent,
        )

        Categories(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Normal)
                .padding(horizontal = Large),
            component = component,
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

        Limits(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Normal)
                .padding(horizontal = Large),
            component = component,
        )
    }
}

@Composable
private fun Categories(
    modifier: Modifier,
    component: SettingsComponentInternal,
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = CardDefaults.shape,
            )
            .clip(CardDefaults.shape)
            .padding(Normal),
    ) {
        Text(
            text = stringResource(Resources.string.categories),
            style = MaterialTheme.typography.titleMedium,
        )
        Row(
            modifier = Modifier.padding(top = Normal),
            horizontalArrangement = Arrangement.spacedBy(Normal),
        ) {
            Button(onClick = { component.openCategories(TransactionType.Income) }) {
                Text(stringResource(Resources.string.income))
            }
            Button(onClick = { component.openCategories(TransactionType.Expense) }) {
                Text(stringResource(Resources.string.expense))
            }
        }
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

@Composable
private fun Limits(
    modifier: Modifier,
    component: SettingsComponentInternal,
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = CardDefaults.shape,
            )
            .clip(shape = CardDefaults.shape)
            .debounceClickable(onClick = component::onLimitsClicked)
            .padding(Normal),
    ) {
        Text(
            text = stringResource(Resources.string.limits),
            style = MaterialTheme.typography.titleMedium,
        )

        Text(
            modifier = Modifier.padding(top = Normal),
            text = stringResource(Resources.string.limits_description),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
