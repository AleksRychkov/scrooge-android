package dev.aleksrychkov.scrooge.component.root.internal

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.main.MainComponent

internal interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class Main(val component: MainComponent) : Child()
    }
}
