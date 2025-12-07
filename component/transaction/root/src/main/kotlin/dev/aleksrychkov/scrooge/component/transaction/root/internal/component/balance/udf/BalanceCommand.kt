package dev.aleksrychkov.scrooge.component.transaction.root.internal.component.balance.udf

import kotlin.time.Instant

internal sealed interface BalanceCommand {
    data class UpdateBalance(val instant: Instant) : BalanceCommand
}
