package dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.LimitEntity
import kotlinx.collections.immutable.ImmutableList

internal sealed interface LimitsEvent {
    sealed interface External : LimitsEvent {
        data object Init : External
        data object AddNewLimit : External
        data class DeleteLimit(val id: Long) : External
        data class AmountChanged(val id: Long, val value: String) : External
    }

    sealed interface Internal : LimitsEvent {
        data class LimitsResult(val data: ImmutableList<LimitEntity>) : Internal
        data class CurrencyResult(val currency: CurrencyEntity) : Internal
        data object Reload : External
    }
}
