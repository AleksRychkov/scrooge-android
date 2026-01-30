package dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf

import dev.aleksrychkov.scrooge.core.di.get
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith

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
                    listOf(LimitsCommand.LoadLimits)
                }
            }

            is LimitsEvent.Internal.LimitsResult -> state.reduceWith(event) {
                state {
                    copy(
                        isLoading = false,
                        limits = event.data,
                        editable = event.data.toState(resourceManager),
                    )
                }
            }

            LimitsEvent.External.Save -> state.reduceWith(event) {
                command {
                    listOf(LimitsCommand.Save(state.editable.toEntity(resourceManager)))
                }
                state {
                    copy(isLoading = true)
                }
            }
        }
    }
}
