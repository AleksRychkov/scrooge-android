package dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.resources.CategoryIcon
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Immutable
internal data class TransactionsListState(
    val isLoading: Boolean = false,
    val pagedTransactions: Flow<PagingData<TransactionsItem>> = emptyFlow(),
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
