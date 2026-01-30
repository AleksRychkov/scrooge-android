package dev.aleksrychkov.scrooge.presentation.screen.settings.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import dev.aleksrychkov.scrooge.core.router.DestinationLimits
import dev.aleksrychkov.scrooge.core.router.Router
import dev.aleksrychkov.scrooge.core.router.context.RouterComponentContext
import dev.aleksrychkov.scrooge.presentation.component.settingstheme.SettingsThemeComponent
import dev.aleksrychkov.scrooge.presentation.component.settingstransfer.export.TransferExportComponent
import dev.aleksrychkov.scrooge.presentation.component.settingstransfer.import.TransferImportComponent

internal class DefaultSettingsComponent(
    private val componentContext: ComponentContext
) : SettingsComponentInternal, ComponentContext by componentContext {

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
}
