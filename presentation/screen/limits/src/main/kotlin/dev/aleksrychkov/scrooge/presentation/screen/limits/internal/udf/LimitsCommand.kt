package dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf

import dev.aleksrychkov.scrooge.core.entity.LimitEntity

internal sealed interface LimitsCommand {
    data object LoadLimits : LimitsCommand
    data object LoadLastUsedCurrency : LimitsCommand
    data class Update(val entity: LimitEntity) : LimitsCommand
    data class Delete(val id: Long) : LimitsCommand
    data class Create(val entity: LimitEntity) : LimitsCommand
}
