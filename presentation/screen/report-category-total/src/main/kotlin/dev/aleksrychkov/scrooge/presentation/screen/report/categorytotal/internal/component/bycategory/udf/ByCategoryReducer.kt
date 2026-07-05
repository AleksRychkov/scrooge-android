package dev.aleksrychkov.scrooge.presentation.screen.report.categorytotal.internal.component.bycategory.udf

import dev.aleksrychkov.scrooge.core.di.get
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import dev.aleksrychkov.scrooge.presentation.screen.report.categorytotal.internal.component.bycategory.udf.ByCategoryCommand.Load

internal class ByCategoryReducer(
    private val resourceManager: ResourceManager = get(),
) : Reducer<ByCategoryState, ByCategoryEvent, ByCategoryCommand, Unit> {
    override fun reduce(
        event: ByCategoryEvent,
        state: ByCategoryState
    ): ReducerResult<ByCategoryState, ByCategoryCommand, Unit> {
        return when (event) {
            is ByCategoryEvent.External.Load -> state.reduceWith(event) {
                state {
                    copy(isLoading = true, filter = event.filter)
                }
                command {
                    listOf(Load(event.filter))
                }
            }

            ByCategoryEvent.External.Reload -> state.reduceWith(event) {
                command {
                    listOf(Load(filter))
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
                        byCurrencyIncome = event.result.income
                            .toByCurrencyStateList(resourceManager = resourceManager),
                        byCurrencyExpense = event.result.expense
                            .toByCurrencyStateList(resourceManager = resourceManager),
                    )
                }
            }

            is ByCategoryEvent.External.BottomSheetOffset -> state.reduceWith(event) {
                state {
                    copy(
                        bottomSheetOffset = event.offset,
                    )
                }
            }
        }
    }
}
