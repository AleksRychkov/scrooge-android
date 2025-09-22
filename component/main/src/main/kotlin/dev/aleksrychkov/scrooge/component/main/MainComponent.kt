package dev.aleksrychkov.scrooge.component.main

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.main.internal.DefaultMainComponent

interface MainComponent {
    companion object {
        operator fun invoke(componentContext: ComponentContext): MainComponent {
            return DefaultMainComponent(componentContext)
        }
    }
}
