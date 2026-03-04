package dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf

import dev.aleksrychkov.scrooge.core.di.get
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import kotlinx.collections.immutable.toImmutableList

internal class LimitsReducer(
    private val resourceManager: ResourceManager = get(),
) : Reducer<LimitsState, LimitsEvent, LimitsCommand, Unit> {
    override fun reduce(
        event: LimitsEvent,
        state: LimitsState
    ): ReducerResult<LimitsState, LimitsCommand, Unit> {
        return when (event) {
            is LimitsEvent.Internal.LimitsResult -> state.reduceWith(event) {
                state {
                    copy(
                        isVisible = event.limits.isNotEmpty(),
                        limits = event.limits
                            .map { it.toLimitProgress(resourceManager) }
                            .toImmutableList()
                    )
                }
            }

            is LimitsEvent.External.Load -> state.reduceWith(event) {
                command {
                    listOf(LimitsCommand.ObserveLimits(filter = event.filter))
                }
            }
        }
    }
}
