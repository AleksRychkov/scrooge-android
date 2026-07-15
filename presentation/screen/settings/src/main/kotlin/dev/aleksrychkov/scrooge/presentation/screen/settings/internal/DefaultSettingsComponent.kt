package dev.aleksrychkov.scrooge.presentation.screen.settings.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.router.DestinationLimits
import dev.aleksrychkov.scrooge.core.router.Router
import dev.aleksrychkov.scrooge.core.router.context.RouterComponentContext
import dev.aleksrychkov.scrooge.presentation.component.category.CategoryComponent
import dev.aleksrychkov.scrooge.presentation.component.settingstheme.SettingsThemeComponent
import dev.aleksrychkov.scrooge.presentation.component.settingstransfer.export.TransferExportComponent
import dev.aleksrychkov.scrooge.presentation.component.settingstransfer.import.TransferImportComponent

internal class DefaultSettingsComponent(
    private val componentContext: ComponentContext
) : SettingsComponentInternal, ComponentContext by componentContext {
    private val categoryNavigation = SlotNavigation<TransactionType>()

    override val categoryModal: Value<ChildSlot<*, CategoryComponent>> = childSlot(
        source = categoryNavigation,
        serializer = null,
        handleBackButton = true,
        key = "SettingsCategoryModalSlot",
    ) { type, childComponentContext ->
        CategoryComponent(
            componentContext = childComponentContext,
            transactionType = type,
        )
    }

    private val router: Router by lazy {
        (componentContext as RouterComponentContext).router
    }

    private val _settingsThemeComponent: SettingsThemeComponent by lazy {
        SettingsThemeComponent(
            componentContext = childContext("SettingsComponentThemeComponent")
        )
    }

    private val _transferImportComponent: TransferImportComponent by lazy {
        TransferImportComponent(
            componentContext = childContext("SettingsComponentTransferImportComponent")
        )
    }

    private val _transferExportComponent: TransferExportComponent by lazy {
        TransferExportComponent(
            componentContext = childContext("SettingsComponentTransferExportComponent")
        )
    }

    override val settingsThemeComponent: SettingsThemeComponent
        get() = _settingsThemeComponent

    override val transferImportComponent: TransferImportComponent
        get() = _transferImportComponent

    override val transferExportComponent: TransferExportComponent
        get() = _transferExportComponent

    override fun onLimitsClicked() {
        router.open(DestinationLimits)
    }

    override fun openCategories(type: TransactionType) {
        categoryNavigation.activate(type)
    }

    override fun closeCategories() {
        categoryNavigation.dismiss()
    }
}
