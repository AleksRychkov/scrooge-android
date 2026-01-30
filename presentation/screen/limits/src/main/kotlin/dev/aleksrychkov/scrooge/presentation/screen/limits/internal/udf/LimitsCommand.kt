package dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf

import dev.aleksrychkov.scrooge.core.entity.LimitEntity

internal sealed interface LimitsCommand {
    data object LoadLimits : LimitsCommand
    data class Save(val limits: List<LimitEntity>) : LimitsCommand
}
