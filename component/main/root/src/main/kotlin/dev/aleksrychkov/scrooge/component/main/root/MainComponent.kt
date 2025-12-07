package dev.aleksrychkov.scrooge.component.main.root

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.main.root.internal.DefaultMainComponent

interface MainComponent {
    companion object {
        operator fun invoke(componentContext: ComponentContext): MainComponent {
            return DefaultMainComponent(componentContext)
        }
    }
}
