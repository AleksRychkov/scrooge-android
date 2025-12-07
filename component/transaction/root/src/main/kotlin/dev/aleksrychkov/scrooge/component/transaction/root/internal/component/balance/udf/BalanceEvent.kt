package dev.aleksrychkov.scrooge.component.transaction.root.internal.component.balance.udf

import dev.aleksrychkov.scrooge.core.entity.ReportAmountForPeriodByTypeAndCodeEntity
import kotlin.time.Instant

internal sealed interface BalanceEvent {
    sealed interface External : BalanceEvent {
        data class SetPeriod(val instant: Instant) : External
    }

    sealed interface Internal : BalanceEvent {
        data object FailedToUpdateBalance : Internal
        data class UpdateBalance(val result: ReportAmountForPeriodByTypeAndCodeEntity) : Internal
    }
}
