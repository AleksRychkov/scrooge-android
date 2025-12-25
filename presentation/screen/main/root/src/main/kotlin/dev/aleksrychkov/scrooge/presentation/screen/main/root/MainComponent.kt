package dev.aleksrychkov.scrooge.presentation.screen.main.root

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.presentation.screen.main.root.internal.DefaultMainComponent

interface MainComponent {
    companion object {
        operator fun invoke(componentContext: ComponentContext): MainComponent {
            return DefaultMainComponent(componentContext)
        }
    }
}
