package dev.aleksrychkov.scrooge.component.report.root.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.report.annualtotal.ReportAnnualTotalComponent
import kotlinx.serialization.Serializable

internal class DefaultReportComponent(
    private val componentContext: ComponentContext
) : ReportComponentInternal, ComponentContext by componentContext {
    private val navigation = StackNavigation<Configuration>()

    override val stack: Value<ChildStack<*, ReportComponentInternal.Child>> =
        childStack(
            source = navigation,
            serializer = Configuration.serializer(),
            initialConfiguration = Configuration.Annual,
            handleBackButton = false,
            key = "DefaultReportComponentStack",
            childFactory = ::child,
        )

    private fun child(
        configuration: Configuration,
        childComponentContext: ComponentContext
    ): ReportComponentInternal.Child =
        when (configuration) {
            Configuration.Annual -> ReportComponentInternal.Child.Annual(
                component = ReportAnnualTotalComponent(componentContext = childComponentContext)
            )
        }

    @Serializable
    private sealed interface Configuration {
        @Serializable
        data object Annual : Configuration
    }
}
