package dev.aleksrychkov.scrooge.component.transactions.internal.component.period

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.Instant

internal interface PeriodComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
            instant: Instant,
        ): PeriodComponent {
            return DefaultPeriodComponent(
                componentContext = componentContext,
                instant = instant,
            )
        }
    }

    val state: StateFlow<PeriodState>

    fun incrementYear()
    fun decrementYear()
    fun monthSelected(month: Int): Instant
}

private class DefaultPeriodComponent(
    componentContext: ComponentContext,
    instant: Instant,
) : PeriodComponent, ComponentContext by componentContext {

    private val _state = MutableStateFlow(PeriodState(instant))

    override val state: StateFlow<PeriodState>
        get() = _state.asStateFlow()

    override fun incrementYear() {
        _state.value = _state.value.incrementYear()
    }

    override fun decrementYear() {
        _state.value = _state.value.decrementYear()
    }

    override fun monthSelected(month: Int): Instant {
        return _state.value.setMonth(month = month).selected
    }
}
