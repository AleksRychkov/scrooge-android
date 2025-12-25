package dev.aleksrychkov.scrooge.dev.aleksrychkov.scrooge.presentation.screen.root.internal

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.presentation.screen.main.root.MainComponent

internal interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class Main(val component: MainComponent) : Child()
    }
}
