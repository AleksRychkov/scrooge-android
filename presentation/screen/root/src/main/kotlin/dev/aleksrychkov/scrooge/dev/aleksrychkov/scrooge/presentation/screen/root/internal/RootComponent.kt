package dev.aleksrychkov.scrooge.dev.aleksrychkov.scrooge.presentation.screen.root.internal

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.presentation.screen.main.root.MainComponent
import dev.aleksrychkov.scrooge.presentation.screen.transfer.RootTransferComponent

internal interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class Intermediate : Child()
        class Main(val component: MainComponent) : Child()
        class Transfer(val component: RootTransferComponent) : Child()
    }
}
