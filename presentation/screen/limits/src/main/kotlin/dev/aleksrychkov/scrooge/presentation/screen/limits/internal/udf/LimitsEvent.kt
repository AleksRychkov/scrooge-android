package dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf

import dev.aleksrychkov.scrooge.core.entity.LimitEntity
import kotlinx.collections.immutable.ImmutableList

internal sealed interface LimitsEvent {
    sealed interface External : LimitsEvent {
        data object Init : External
        data object Save : External
    }

    sealed interface Internal : LimitsEvent {
        data class LimitsResult(val data: ImmutableList<LimitEntity>) : Internal
    }
}
