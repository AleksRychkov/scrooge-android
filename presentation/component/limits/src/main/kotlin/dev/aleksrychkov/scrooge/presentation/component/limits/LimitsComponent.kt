package dev.aleksrychkov.scrooge.presentation.component.limits

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.presentation.component.limits.internal.DefaultLimitsComponent

interface LimitsComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext
        ): LimitsComponent = DefaultLimitsComponent(componentContext = componentContext)
    }

    fun setFilter(filter: FilterEntity)
}
