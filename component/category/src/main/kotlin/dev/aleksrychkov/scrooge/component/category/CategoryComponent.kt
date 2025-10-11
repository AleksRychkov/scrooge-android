package dev.aleksrychkov.scrooge.component.category

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.category.internal.DefaultCategoryComponent
import dev.aleksrychkov.scrooge.core.entity.TransactionType

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
