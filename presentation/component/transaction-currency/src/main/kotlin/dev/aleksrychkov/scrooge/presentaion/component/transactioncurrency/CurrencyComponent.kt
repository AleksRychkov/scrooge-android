package dev.aleksrychkov.scrooge.presentaion.component.transactioncurrency

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.presentaion.component.transactioncurrency.internal.DefaultCurrencyComponent

interface CurrencyComponent {
    companion object {
        operator fun invoke(componentContext: ComponentContext): CurrencyComponent {
            return DefaultCurrencyComponent(componentContext = componentContext)
        }
    }
}
