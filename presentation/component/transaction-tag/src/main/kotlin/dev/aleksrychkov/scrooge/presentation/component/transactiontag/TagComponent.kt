package dev.aleksrychkov.scrooge.presentation.component.transactiontag

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.DefaultTagComponent

interface TagComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
            isEditable: Boolean = true,
        ): TagComponent = DefaultTagComponent(
            componentContext = componentContext,
            isEditable = isEditable,
        )
    }
}
