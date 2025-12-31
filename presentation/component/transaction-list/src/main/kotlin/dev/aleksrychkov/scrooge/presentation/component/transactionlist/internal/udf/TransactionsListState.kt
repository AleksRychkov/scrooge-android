package dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.resources.CategoryIcon
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Immutable
internal data class TransactionsListState(
    val isLoading: Boolean = false,
    val transactions: ImmutableList<TransactionsGroupDto> = persistentListOf(),
//    val pagedTransactions: Flow<PagingData<TransactionsItemDto>> = emptyFlow(),
    val pagedTransactions: Flow<PagingData<TransactionsItem>> = emptyFlow(),
    val scrollIndex: Int = 0,
    val scrollOffset: Int = 0,
)

@Immutable
internal sealed interface TransactionsItem {
    @Immutable
    data class Group(
        val date: String = "",
        val totals: ImmutableList<String> = persistentListOf(),
    ) : TransactionsItem

    @Immutable
    data class Item(
        val id: Long,
        val categoryName: String,
        val categoryIcon: CategoryIcon,
        val categoryColor: Int,
        val amount: String,
        val type: TransactionType,
        val tags: String,
        val date: String,
    ) : TransactionsItem
}

@Immutable
internal data class TransactionsGroupDto(
    val date: String = "",
    val totals: ImmutableList<String> = persistentListOf(),
    val transactions: ImmutableList<TransactionsItemDto> = persistentListOf(),
)

@Immutable
internal data class TransactionsItemDto(
    val categoryName: String,
    val categoryIcon: CategoryIcon,
    val categoryColor: Int,
    val amount: String,
    val type: TransactionType,
    val ref: TransactionEntity,
    val tags: String,
    val date: String = "",
)
