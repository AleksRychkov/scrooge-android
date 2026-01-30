package dev.aleksrychkov.scrooge.presentation.screen.limits

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.DefaultLimitsComponent

interface LimitsComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
        ): LimitsComponent =
            DefaultLimitsComponent(componentContext = componentContext)
    }
}
