package dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf

import dev.aleksrychkov.scrooge.core.di.get
import dev.aleksrychkov.scrooge.core.entity.LimitEntity
import dev.aleksrychkov.scrooge.core.entity.toCents
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsCommand.Create
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsCommand.Delete
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsCommand.LoadLastUsedCurrency
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsCommand.LoadLimits
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsCommand.Update
import kotlinx.collections.immutable.toImmutableList

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
                    listOf(LoadLimits, LoadLastUsedCurrency)
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

            is LimitsEvent.External.AmountChanged -> state.reduceWith(event) {
                val dto = state.editable.firstOrNull { it.id == event.id } ?: return@reduceWith
                val entity = dto.toEntity(resourceManager).copy(amount = event.value.toCents())
                command {
                    listOf(Update(entity = entity))
                }
            }

            LimitsEvent.Internal.Reload -> state.reduceWith(event) {
                command {
                    listOf(LoadLimits)
                }
            }

            is LimitsEvent.External.PeriodChanged -> state.reduceWith(event) {
                val editable = state.editable.toMutableList()
                val dto = editable
                    .firstOrNull { it.id == event.id }?.copy(periodText = event.value)
                    ?: return@reduceWith
                val index = editable.indexOfFirst { it.id == dto.id }
                editable[index] = dto
                val entity = dto.toEntity(resourceManager)
                command {
                    listOf(Update(entity = entity))
                }
                state {
                    copy(editable = editable.toImmutableList())
                }
            }
        }
    }
}
