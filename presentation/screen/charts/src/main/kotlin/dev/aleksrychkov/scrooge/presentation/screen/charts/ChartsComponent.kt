package dev.aleksrychkov.scrooge.presentation.screen.charts

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.presentation.screen.charts.internal.DefaultChartsComponent

interface ChartsComponent {
    companion object {
        operator fun invoke(componentContext: ComponentContext): ChartsComponent =
            DefaultChartsComponent(componentContext)
    }
}
