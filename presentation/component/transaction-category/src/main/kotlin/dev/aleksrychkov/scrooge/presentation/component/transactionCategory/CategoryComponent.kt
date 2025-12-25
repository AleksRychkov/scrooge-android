package dev.aleksrychkov.scrooge.presentation.component.transactionCategory

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.DefaultCategoryComponent

interface CategoryComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
            transactionType: TransactionType,
        ): CategoryComponent = DefaultCategoryComponent(
            componentContext = componentContext,
            transactionType = transactionType,
        )
    }
}
