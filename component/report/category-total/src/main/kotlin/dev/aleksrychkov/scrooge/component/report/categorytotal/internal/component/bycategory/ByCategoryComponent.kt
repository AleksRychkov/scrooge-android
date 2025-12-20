package dev.aleksrychkov.scrooge.component.report.categorytotal.internal.component.bycategory

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.report.categorytotal.internal.component.bycategory.udf.ByCategoryState
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal interface ByCategoryComponent {
    companion object Companion {
        operator fun invoke(
            componentContext: ComponentContext,
            period: PeriodTimestampEntity,
        ): ByCategoryComponent = DefaultByCategoryComponent(
            componentContext = componentContext,
            period = period,
        )
    }

    val state: StateFlow<ByCategoryState>
}

private class DefaultByCategoryComponent(
    componentContext: ComponentContext,
    period: PeriodTimestampEntity
) : ByCategoryComponent, ComponentContext by componentContext {

    override val state: StateFlow<ByCategoryState> = MutableStateFlow(ByCategoryState())
}
