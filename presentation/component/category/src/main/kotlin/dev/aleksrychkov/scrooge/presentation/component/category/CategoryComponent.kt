package dev.aleksrychkov.scrooge.presentation.component.category

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.presentation.component.category.internal.DefaultCategoryComponent

interface CategoryComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
            transactionType: TransactionType?,
            isEditable: Boolean = true,
        ): CategoryComponent = DefaultCategoryComponent(
            componentContext = componentContext,
            transactionType = transactionType,
            isEditable = isEditable,
        )
    }
}
