package dev.aleksrychkov.scrooge.component.category.internal.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class CategoryState(
    val transactionType: TransactionType = TransactionType.Income,
    val categories: ImmutableList<CategoryEntity> = persistentListOf(),
    val filtered: ImmutableList<CategoryEntity> = persistentListOf(),
    val searchQuery: String = "",
)
