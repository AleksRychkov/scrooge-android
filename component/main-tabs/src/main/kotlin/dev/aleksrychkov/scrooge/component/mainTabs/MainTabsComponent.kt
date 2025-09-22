package dev.aleksrychkov.scrooge.component.mainTabs

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.mainTabs.internal.DefaultMainTabsComponent

interface MainTabsComponent {
    companion object Companion {
        operator fun invoke(componentContext: ComponentContext): MainTabsComponent {
            return DefaultMainTabsComponent(componentContext)
        }
    }
}
