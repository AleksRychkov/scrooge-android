package dev.aleksrychkov.scrooge.component.transactions.internal

import com.arkivanov.decompose.ComponentContext

internal class DefaultTransactionsComponent(
    private val componentContext: ComponentContext
) : TransactionsComponentInternal, ComponentContext by componentContext
