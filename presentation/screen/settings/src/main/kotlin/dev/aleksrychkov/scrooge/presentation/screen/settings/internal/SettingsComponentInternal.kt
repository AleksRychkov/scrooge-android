package dev.aleksrychkov.scrooge.presentation.screen.settings.internal

import dev.aleksrychkov.scrooge.presentation.component.settingstheme.SettingsThemeComponent
import dev.aleksrychkov.scrooge.presentation.component.settingstransfer.export.TransferExportComponent
import dev.aleksrychkov.scrooge.presentation.component.settingstransfer.import.TransferImportComponent
import dev.aleksrychkov.scrooge.presentation.screen.settings.SettingsComponent

internal interface SettingsComponentInternal : SettingsComponent {
    val settingsThemeComponent: SettingsThemeComponent
    val transferImportComponent: TransferImportComponent
    val transferExportComponent: TransferExportComponent

    fun onLimitsClicked()
}
