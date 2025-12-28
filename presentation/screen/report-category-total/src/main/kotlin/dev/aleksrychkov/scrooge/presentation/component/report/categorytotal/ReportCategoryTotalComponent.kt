package dev.aleksrychkov.scrooge.presentation.component.report.categorytotal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal.DefaultReportCategoryTotalComponent

interface ReportCategoryTotalComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
            filter: FilterEntity,
        ): ReportCategoryTotalComponent {
            return DefaultReportCategoryTotalComponent(
                componentContext = componentContext,
                filter = filter,
            )
        }
    }
}
