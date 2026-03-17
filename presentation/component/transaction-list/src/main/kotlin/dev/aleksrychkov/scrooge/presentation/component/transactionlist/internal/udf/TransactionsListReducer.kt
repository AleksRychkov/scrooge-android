package dev.aleksrychkov.scrooge.presentation.component.transactionlist.internal.udf

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import dev.aleksrychkov.scrooge.core.di.get
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.entity.readableName
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import dev.aleksrychkov.scrooge.core.resources.R as Resources

internal class TransactionsListReducer(
    private val scope: CoroutineScope,
    private val resourceManager: ResourceManager = get(),
) :
    Reducer<TransactionsListState, TransactionsListEvent, TransactionsListCommand, Unit> {

    private val mapper: TransactionsListMapper by lazy {
        TransactionsListMapper()
    }

    private val dateToday: String by lazy {
        resourceManager.getString(Resources.string.today)
    }

    private val dateYesterday: String by lazy {
        resourceManager.getString(Resources.string.yesterday)
    }

    override fun reduce(
        event: TransactionsListEvent,
        state: TransactionsListState
    ): ReducerResult<TransactionsListState, TransactionsListCommand, Unit> {
        return when (event) {
            is TransactionsListEvent.External.Initial -> state.reduceWith(event) {
                command {
                    listOf(
                        TransactionsListCommand.PreloadCategories,
                    )
                }
            }

            is TransactionsListEvent.External.SetFilter -> state.reduceWith(event) {
                command {
                    listOf(TransactionsListCommand.LoadTransactions(filter = event.filter))
                }
                state {
                    copy(isLoading = true)
                }
            }

            is TransactionsListEvent.Internal.PagedTransactions -> state.reduceWith(event) {
                val pagedTransactions: Flow<PagingData<TransactionsItem>> = event.data
                    .map { pagingData: PagingData<TransactionEntity> ->
                        val transactions = mutableMapOf<String, MutableList<TransactionEntity>>()
                        pagingData
                            .map { entity ->
                                val itemDate = entity.datestamp.readableName(
                                    today = dateToday,
                                    yesterday = dateYesterday,
                                )
                                val map = transactions[itemDate] ?: mutableListOf()
                                map.add(entity)
                                transactions[itemDate] = map

                                mapper.transactionToUiItem(entity = entity, date = itemDate)
                            }
                            .insertSeparators { before, after ->
                                when {
                                    before == null && after != null ->
                                        TransactionsItem.Group(
                                            date = after.date,
                                            totals = mapper.calculateTotals(
                                                transactions[after.date] ?: emptyList()
                                            ),
                                        )

                                    before != null && after != null && before.date != after.date ->
                                        TransactionsItem.Group(
                                            date = after.date,
                                            totals = mapper.calculateTotals(
                                                transactions[after.date] ?: emptyList()
                                            ),
                                        )

                                    else -> null
                                }
                            }
                    }
                state {
                    copy(
                        isLoading = false,
                        pagedTransactions = pagedTransactions.cachedIn(scope),
                    )
                }
            }
        }
    }
}
