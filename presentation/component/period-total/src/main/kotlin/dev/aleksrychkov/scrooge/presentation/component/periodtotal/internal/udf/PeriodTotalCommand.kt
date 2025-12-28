package dev.aleksrychkov.scrooge.presentation.component.periodtotal.internal.udf

import dev.aleksrychkov.scrooge.core.entity.FilterEntity

sealed interface PeriodTotalCommand {
    data class Load(val filter: FilterEntity) : PeriodTotalCommand
}
