package dev.aleksrychkov.scrooge.component.category.internal.component.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.resources.CategoryIcon
import dev.aleksrychkov.scrooge.core.resources.UncategorizedIcon
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class CreateCategoryState(
    val isLoading: Boolean = false,
    val isDone: Boolean = false,
    val name: String = "",
    val transactionType: TransactionType = TransactionType.Expense,
    val selectedCategoryIcon: CategoryIcon = UncategorizedIcon,
    val availableIcons: ImmutableList<CategoryIcon> = persistentListOf(),
)
