package dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf

import dev.aleksrychkov.scrooge.core.entity.LimitDataEntity
import kotlinx.collections.immutable.ImmutableList

sealed interface LimitsEvent {
    sealed interface External : LimitsEvent {
        data object Init : External
    }

    sealed interface Internal : LimitsEvent {
        data class LimitsResult(val limits: ImmutableList<LimitDataEntity>) : Internal
    }
}
