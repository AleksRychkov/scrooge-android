package dev.aleksrychkov.scrooge.component.main

import com.arkivanov.decompose.ComponentContext

interface MainComponent {
    companion object {
        operator fun invoke(componentContext: ComponentContext): MainComponent {
            return object : MainComponent {}
        }
    }
}
