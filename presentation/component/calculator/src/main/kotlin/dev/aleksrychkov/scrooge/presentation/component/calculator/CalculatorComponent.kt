package dev.aleksrychkov.scrooge.presentation.component.calculator

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.presentation.component.calculator.internal.DefaultCalculatorComponent

interface CalculatorComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext
        ): CalculatorComponent =
            DefaultCalculatorComponent(componentContext = componentContext)
    }
}
