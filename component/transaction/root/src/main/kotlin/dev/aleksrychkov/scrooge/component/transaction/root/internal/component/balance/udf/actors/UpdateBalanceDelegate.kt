package dev.aleksrychkov.scrooge.component.transaction.root.internal.component.balance.udf.actors

import dev.aleksrychkov.scrooge.component.transaction.root.internal.component.balance.udf.BalanceCommand
import dev.aleksrychkov.scrooge.component.transaction.root.internal.component.balance.udf.BalanceEvent
import dev.aleksrychkov.scrooge.component.transaction.root.internal.utils.DateTimeUtils
import dev.aleksrychkov.scrooge.feature.reports.ReportTotalByTypeAndCurrencyResult
import dev.aleksrychkov.scrooge.feature.reports.ReportTotalByTypeAndCurrencyUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

internal class UpdateBalanceDelegate(
    private val useCase: Lazy<ReportTotalByTypeAndCurrencyUseCase>,
) {

    suspend operator fun invoke(cmd: BalanceCommand.UpdateBalance): Flow<BalanceEvent> {
        val pair = DateTimeUtils.getMonthStartEndTimestamp(cmd.instant)
        val result = useCase.value.invoke(
            fromTimestamp = pair.first,
            toTimestamp = pair.second,
        )
        return when (result) {
            ReportTotalByTypeAndCurrencyResult.Failure -> flowOf(
                BalanceEvent.Internal.FailedToUpdateBalance
            )

            is ReportTotalByTypeAndCurrencyResult.Success -> result.flow.map {
                BalanceEvent.Internal.UpdateBalance(it)
            }
        }
    }
}
