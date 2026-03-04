package dev.aleksrychkov.scrooge.presentation.screen.main.root

import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.presentation.screen.main.root.internal.DefaultMainComponent

interface MainComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
            deeplink: String? = null,
        ): MainComponent {
            return DefaultMainComponent(
                componentContext = componentContext,
                deeplink = deeplink?.let(Uri::parse),
            )
        }
    }

    fun handleDeeplink(deeplink: String)
}
