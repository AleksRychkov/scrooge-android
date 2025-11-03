package dev.aleksrychkov.scrooge.component.transactions.internal.component.balance

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.component.transactions.internal.component.balance.udf.BalanceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Instant

internal interface BalanceComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
        ): BalanceComponent {
            return DefaultBalanceComponent(componentContext = componentContext)
        }
    }

    val state: StateFlow<BalanceState>

    fun setPeriod(instant: Instant)
}

private class DefaultBalanceComponent(
    componentContext: ComponentContext
) : BalanceComponent, ComponentContext by componentContext {

    override val state: StateFlow<BalanceState>
        get() = MutableStateFlow(BalanceState())

    override fun setPeriod(instant: Instant) {
        // todo
    }
}
