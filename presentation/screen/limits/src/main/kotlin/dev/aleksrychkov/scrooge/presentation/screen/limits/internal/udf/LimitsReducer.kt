package dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf

import dev.aleksrychkov.scrooge.core.di.get
import dev.aleksrychkov.scrooge.core.entity.LimitEntity
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsCommand.Create
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsCommand.Delete

internal class LimitsReducer(
    private val resourceManager: ResourceManager = get(),
) : Reducer<LimitsState, LimitsEvent, LimitsCommand, Unit> {

    override fun reduce(
        event: LimitsEvent,
        state: LimitsState
    ): ReducerResult<LimitsState, LimitsCommand, Unit> {
        return when (event) {
            LimitsEvent.External.Init -> state.reduceWith(event) {
                command {
                    listOf(LimitsCommand.LoadLimits, LimitsCommand.LoadLastUsedCurrency)
                }
            }

            is LimitsEvent.Internal.LimitsResult -> state.reduceWith(event) {
                state {
                    copy(
                        isLoading = false,
                        editable = event.data.toState(resourceManager),
                    )
                }
            }

            LimitsEvent.External.AddNewLimit -> state.reduceWith(event) {
                val entity = LimitEntity(
                    id = -1L,
                    currency = state.lastUsedCurrencyEntity,
                    amount = 0L,
                    period = LimitEntity.Period.Monthly,
                )
                command {
                    listOf(Create(entity = entity))
                }
            }

            is LimitsEvent.External.DeleteLimit -> state.reduceWith(event) {
                command {
                    listOf(Delete(id = event.id))
                }
            }

            is LimitsEvent.Internal.CurrencyResult -> state.reduceWith(event) {
                state {
                    copy(lastUsedCurrencyEntity = event.currency)
                }
            }
        }
    }
}
