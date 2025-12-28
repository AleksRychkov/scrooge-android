package dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal.component.bycategory.udf.actors

import dev.aleksrychkov.scrooge.feature.reports.ReportByCategoryResult
import dev.aleksrychkov.scrooge.feature.reports.ReportByCategoryUseCase
import dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal.component.bycategory.udf.ByCategoryCommand
import dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal.component.bycategory.udf.ByCategoryEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class LoadDelegate(
    private val useCase: Lazy<ReportByCategoryUseCase>
) {
    suspend operator fun invoke(cmd: ByCategoryCommand.Load): Flow<ByCategoryEvent> {
        return when (val result = useCase.value.invoke(cmd.filter.period)) {
            ReportByCategoryResult.Failure ->
                flowOf(ByCategoryEvent.Internal.LoadFailed)

            is ReportByCategoryResult.Success ->
                flowOf(ByCategoryEvent.Internal.LoadSuccess(result.result))
        }
    }
}
