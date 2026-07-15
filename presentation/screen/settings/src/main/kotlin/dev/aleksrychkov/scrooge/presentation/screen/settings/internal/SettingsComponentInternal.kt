package dev.aleksrychkov.scrooge.presentation.screen.settings.internal

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.presentation.component.category.CategoryComponent
import dev.aleksrychkov.scrooge.presentation.component.settingstheme.SettingsThemeComponent
import dev.aleksrychkov.scrooge.presentation.component.settingstransfer.export.TransferExportComponent
import dev.aleksrychkov.scrooge.presentation.component.settingstransfer.import.TransferImportComponent
import dev.aleksrychkov.scrooge.presentation.screen.settings.SettingsComponent

internal interface SettingsComponentInternal : SettingsComponent {
    val settingsThemeComponent: SettingsThemeComponent
    val transferImportComponent: TransferImportComponent
    val transferExportComponent: TransferExportComponent
    val categoryModal: Value<ChildSlot<*, CategoryComponent>>

    fun onLimitsClicked()
    fun openCategories(type: TransactionType)
    fun closeCategories()
}
