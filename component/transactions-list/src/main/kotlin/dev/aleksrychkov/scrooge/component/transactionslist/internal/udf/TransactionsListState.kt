package dev.aleksrychkov.scrooge.component.transactionslist.internal.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.resources.CategoryIcon
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class TransactionsListState(
    val isLoading: Boolean = false,
    val transactions: ImmutableList<TransactionsGroupDto> = persistentListOf(),
    val scrollIndex: Int = 0,
    val scrollOffset: Int = 0,
)

@Immutable
internal data class TransactionsGroupDto(
    val date: String = "",
    val totals: ImmutableList<String> = persistentListOf(),
    val transactions: ImmutableList<TransactionsItemDto> = persistentListOf(),
)

@Immutable
internal data class TransactionsItemDto(
    val category: String,
    val icon: CategoryIcon,
    val amount: String,
    val type: TransactionType,
    val ref: TransactionEntity,
    val tags: String,
)
