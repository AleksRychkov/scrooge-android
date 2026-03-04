package dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf

import dev.aleksrychkov.scrooge.core.entity.FilterEntity

internal sealed interface LimitsCommand {
    data class ObserveLimits(val filter: FilterEntity) : LimitsCommand
}
