package dev.aleksrychkov.scrooge.component.report.root.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.component.report.annualtotal.ReportAnnualTotalComponent
import dev.aleksrychkov.scrooge.component.report.categorytotal.ReportCategoryTotalComponent
import dev.aleksrychkov.scrooge.component.report.root.internal.ReportComponentInternal.Child.AnnualTotal
import dev.aleksrychkov.scrooge.component.report.root.internal.ReportComponentInternal.Child.CategoryTotal
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
import kotlinx.serialization.Serializable

internal class DefaultReportComponent(
    private val componentContext: ComponentContext
) : ReportComponentInternal, ComponentContext by componentContext {
    private val navigation = StackNavigation<Configuration>()

    override val stack: Value<ChildStack<*, ReportComponentInternal.Child>> =
        childStack(
            source = navigation,
            serializer = Configuration.serializer(),
            initialConfiguration = Configuration.AnnualTotal,
            handleBackButton = true,
            key = "DefaultReportComponentStack",
            childFactory = ::child,
        )

    private fun child(
        configuration: Configuration,
        childComponentContext: ComponentContext
    ): ReportComponentInternal.Child =
        when (configuration) {
            Configuration.AnnualTotal -> AnnualTotal(
                component = ReportAnnualTotalComponent(componentContext = childComponentContext)
            )

            is Configuration.CategoryTotal -> CategoryTotal(
                component = ReportCategoryTotalComponent(
                    componentContext = childComponentContext,
                    period = configuration.period,
                )
            )
        }

    override fun openCategoryReport(period: PeriodTimestampEntity) {
        navigation.pushNew(Configuration.CategoryTotal(period))
    }

    @Serializable
    private sealed interface Configuration {
        @Serializable
        data object AnnualTotal : Configuration

        @Serializable
        data class CategoryTotal(val period: PeriodTimestampEntity) : Configuration
    }
}
