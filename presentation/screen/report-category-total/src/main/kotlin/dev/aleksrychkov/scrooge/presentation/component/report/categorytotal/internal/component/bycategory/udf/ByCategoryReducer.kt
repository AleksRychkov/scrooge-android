package dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal.component.bycategory.udf

import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith

internal class ByCategoryReducer :
    Reducer<ByCategoryState, ByCategoryEvent, ByCategoryCommand, Unit> {
    override fun reduce(
        event: ByCategoryEvent,
        state: ByCategoryState
    ): ReducerResult<ByCategoryState, ByCategoryCommand, Unit> {
        return when (event) {
            is ByCategoryEvent.External.Load -> state.reduceWith(event) {
                state {
                    copy(isLoading = true, period = event.period)
                }
                command {
                    listOf(ByCategoryCommand.Load(event.period))
                }
            }

            is ByCategoryEvent.External.SetType -> state.reduceWith(event) {
                state {
                    copy(currentType = TransactionType.from(event.type))
                }
            }

            ByCategoryEvent.Internal.LoadFailed -> state.reduceWith(event) {
                state {
                    copy(isLoading = false)
                }
            }

            is ByCategoryEvent.Internal.LoadSuccess -> state.reduceWith(event) {
                state {
                    copy(
                        isLoading = false,
                        byCurrencyIncome = event.result.income.toByCurrencyStateList(),
                        byCurrencyExpense = event.result.expense.toByCurrencyStateList(),
                    )
                }
            }
        }
    }
}
