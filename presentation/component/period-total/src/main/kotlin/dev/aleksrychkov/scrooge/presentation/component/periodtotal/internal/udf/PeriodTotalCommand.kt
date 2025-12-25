package dev.aleksrychkov.scrooge.presentation.component.periodtotal.internal.udf

import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity

sealed interface PeriodTotalCommand {
    data class Load(val period: PeriodTimestampEntity) : PeriodTotalCommand
}
