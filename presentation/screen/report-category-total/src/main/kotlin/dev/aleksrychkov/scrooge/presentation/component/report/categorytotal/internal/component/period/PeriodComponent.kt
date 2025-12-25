package dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal.component.period

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.di.get
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
import dev.aleksrychkov.scrooge.core.resources.ResourceManager

internal interface PeriodComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
            period: PeriodTimestampEntity
        ): PeriodComponent = DefaultPeriodComponent(
            componentContext = componentContext,
            period = period,
        )
    }

    val state: PeriodState
}

private class DefaultPeriodComponent(
    componentContext: ComponentContext,
    period: PeriodTimestampEntity,
    resourceManager: ResourceManager = get(),
) : PeriodComponent, ComponentContext by componentContext {
    override val state: PeriodState = PeriodState(
        period = period,
        resourceManager = resourceManager,
    )
}
