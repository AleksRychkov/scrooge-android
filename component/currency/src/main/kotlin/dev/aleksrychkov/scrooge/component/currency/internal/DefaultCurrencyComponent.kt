package dev.aleksrychkov.scrooge.component.currency.internal

import com.arkivanov.decompose.ComponentContext

internal class DefaultCurrencyComponent(
    private val componentContext: ComponentContext,
) : CurrencyComponentInternal, ComponentContext by componentContext
