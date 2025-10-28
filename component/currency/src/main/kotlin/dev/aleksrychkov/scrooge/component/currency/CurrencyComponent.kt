package dev.aleksrychkov.scrooge.component.currency

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.currency.internal.DefaultCurrencyComponent

interface CurrencyComponent {
    companion object {
        operator fun invoke(componentContext: ComponentContext): CurrencyComponent {
            return DefaultCurrencyComponent(componentContext = componentContext)
        }
    }
}
