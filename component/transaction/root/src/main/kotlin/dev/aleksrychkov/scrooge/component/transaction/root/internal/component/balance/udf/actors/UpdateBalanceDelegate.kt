package dev.aleksrychkov.scrooge.component.transaction.root.internal.component.balance.udf.actors

import dev.aleksrychkov.scrooge.component.transaction.root.internal.component.balance.udf.BalanceCommand
import dev.aleksrychkov.scrooge.component.transaction.root.internal.component.balance.udf.BalanceEvent
import dev.aleksrychkov.scrooge.component.transaction.root.internal.utils.DateTimeUtils
import dev.aleksrychkov.scrooge.feature.transaction.GetTransactionsResult
import dev.aleksrychkov.scrooge.feature.transaction.GetTransactionsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

internal class UpdateBalanceDelegate(
    private val useCase: Lazy<GetTransactionsUseCase>,
) {

    suspend operator fun invoke(cmd: BalanceCommand.UpdateBalance): Flow<BalanceEvent> {
        val pair = DateTimeUtils.getMonthStartEndTimestamp(cmd.instant)
        val res: GetTransactionsResult = useCase.value.invoke(
            GetTransactionsUseCase.Args(
                fromTimestamp = pair.first,
                toTimestamp = pair.second,
            )
        )
        return when (res) {
            GetTransactionsResult.Failure -> flowOf(BalanceEvent.Internal.FailedToUpdateBalance)
            is GetTransactionsResult.Success -> res.result.map {
                BalanceEvent.Internal.UpdateBalance(it)
            }
        }
    }
}
