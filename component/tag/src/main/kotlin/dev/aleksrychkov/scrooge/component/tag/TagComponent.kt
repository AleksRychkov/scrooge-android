package dev.aleksrychkov.scrooge.component.tag

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.tag.internal.DefaultTagComponent

interface TagComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
        ): TagComponent = DefaultTagComponent(
            componentContext = componentContext,
        )
    }
}
