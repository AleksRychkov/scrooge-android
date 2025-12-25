package dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal.component.bycategory.udf

import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity

internal sealed interface ByCategoryCommand {
    data class Load(val period: PeriodTimestampEntity) : ByCategoryCommand
}
