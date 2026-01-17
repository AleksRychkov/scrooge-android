package dev.aleksrychkov.scrooge.presentation.screen.report.categorytotal.internal.component.bycategory.udf

import dev.aleksrychkov.scrooge.core.entity.FilterEntity

internal sealed interface ByCategoryCommand {
    data class Load(val filter: FilterEntity) : ByCategoryCommand
}
