package dev.aleksrychkov.scrooge.component.transaction.root.internal.component.balance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.component.transaction.root.internal.component.balance.udf.BalanceState
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsBalanceCard

@Composable
internal fun BalanceContent(
    modifier: Modifier,
    component: BalanceComponent,
) {
    val state by component.state.collectAsStateWithLifecycle()
    BalanceContent(
        modifier = modifier,
        state = state,
    )
}

@Composable
private fun BalanceContent(
    modifier: Modifier,
    state: BalanceState,
) {
    DsBalanceCard(
        modifier = modifier,
        data = state.balanceData,
    )
}
