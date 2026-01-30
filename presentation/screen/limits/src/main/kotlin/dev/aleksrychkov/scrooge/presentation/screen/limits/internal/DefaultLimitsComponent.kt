package dev.aleksrychkov.scrooge.presentation.screen.limits.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.router.Router
import dev.aleksrychkov.scrooge.core.router.context.RouterComponentContext

internal class DefaultLimitsComponent(
    componentContext: ComponentContext
) : LimitsComponentInternal, ComponentContext by componentContext {

    private val router: Router by lazy {
        (componentContext as RouterComponentContext).router
    }

    override fun onBackPressed() {
        router.close()
    }
}
