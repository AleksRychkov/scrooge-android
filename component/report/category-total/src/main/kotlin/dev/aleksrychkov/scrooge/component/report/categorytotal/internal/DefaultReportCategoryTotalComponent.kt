package dev.aleksrychkov.scrooge.component.report.categorytotal.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.dismiss
import dev.aleksrychkov.scrooge.component.report.categorytotal.internal.component.bycategory.ByCategoryComponent
import dev.aleksrychkov.scrooge.component.report.categorytotal.internal.component.period.PeriodComponent
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity

@Suppress("UnusedPrivateProperty")
internal class DefaultReportCategoryTotalComponent(
    componentContext: ComponentContext,
    private val period: PeriodTimestampEntity,
) : ReportCategoryTotalComponentInternal, ComponentContext by componentContext {
    private val periodNavigation = SlotNavigation<Unit>()

    private val _periodComponent: PeriodComponent by lazy {
        PeriodComponent(
            componentContext = childContext("ReportCategoryTotalPeriodComponent"),
            period = period,
        )
    }

    private val _byCategoryComponent: ByCategoryComponent by lazy {
        ByCategoryComponent(
            componentContext = childContext("ReportCategoryTotalCategoryComponent"),
            period = period,
        )
    }

    override val periodComponent: PeriodComponent
        get() = _periodComponent

    override val byCategoryComponent: ByCategoryComponent
        get() = _byCategoryComponent

    override fun openPeriodModal() {
        periodNavigation.activate(Unit)
    }

    override fun closePeriodModal() {
        periodNavigation.dismiss()
    }
}
