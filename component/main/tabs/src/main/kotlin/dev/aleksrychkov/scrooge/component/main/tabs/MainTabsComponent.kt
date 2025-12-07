package dev.aleksrychkov.scrooge.component.main.tabs

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.main.tabs.internal.DefaultMainTabsComponent

interface MainTabsComponent {
    companion object Companion {
        operator fun invoke(componentContext: ComponentContext): MainTabsComponent {
            return DefaultMainTabsComponent(componentContext)
        }
    }
}
