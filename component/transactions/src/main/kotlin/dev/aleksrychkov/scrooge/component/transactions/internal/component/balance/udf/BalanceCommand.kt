package dev.aleksrychkov.scrooge.component.transactions.internal.component.balance.udf

import kotlin.time.Instant

internal sealed interface BalanceCommand {
    data class UpdateBalance(val instant: Instant) : BalanceCommand
}
