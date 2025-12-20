package dev.aleksrychkov.scrooge.component.report.categorytotal.internal.component.bycategory.udf

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

            ByCategoryEvent.Internal.LoadFailed -> state.reduceWith(event) {
                state {
                    copy(isLoading = false)
                }
            }

            ByCategoryEvent.Internal.LoadSuccess -> state.reduceWith(event) {
                state {
                    copy(isLoading = false)
                }
            }
        }
    }
}
