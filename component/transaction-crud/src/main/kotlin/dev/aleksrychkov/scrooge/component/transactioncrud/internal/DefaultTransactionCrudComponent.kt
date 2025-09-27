package dev.aleksrychkov.scrooge.component.transactioncrud.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.TransactionType

@Suppress("UnusedPrivateProperty")
internal class DefaultTransactionCrudComponent(
    private val componentContext: ComponentContext,
    private val transactionId: Long? = null,
    private val transactionType: TransactionType? = null,
) : TransactionCrudComponentInternal, ComponentContext by componentContext
