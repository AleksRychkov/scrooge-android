package dev.aleksrychkov.scrooge.presentation.screen.hub

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.presentation.screen.hub.internal.DefaultHubComponent

interface HubComponent {
    companion object Companion {
        operator fun invoke(
            componentContext: ComponentContext
        ): HubComponent = DefaultHubComponent(
            componentContext = componentContext,
        )
    }
}
