package dev.aleksrychkov.scrooge.component.transaction.root.internal.component.balance.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsBalanceData
import kotlin.time.Clock
import kotlin.time.Instant

@Immutable
internal data class BalanceState(
    val isLoading: Boolean = false,
    val balanceData: DsBalanceData = DsBalanceData(),
    val period: Instant = Clock.System.now(),
)
