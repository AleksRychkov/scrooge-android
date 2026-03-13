package dev.aleksrychkov.scrooge.presentation.component.transactionform.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.TransactionType

@Suppress("unused")
internal class DefaultTransactionFormComponent(
    componentContext: ComponentContext,
    private val transactionId: Long?,
    private val type: TransactionType,
) : TransactionFormComponentInternal, ComponentContext by componentContext
