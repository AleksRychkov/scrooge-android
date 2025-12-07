package dev.aleksrychkov.scrooge.component.transaction.root.internal

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.component.transaction.root.internal.utils.DateTimeUtils
import kotlin.time.Clock
import kotlin.time.Instant

@Immutable
internal data class TransactionsState(
    val selectedPeriod: Instant,
    val selectedPeriodReadable: String,
) {
    companion object {
        operator fun invoke(instant: Instant = Clock.System.now()): TransactionsState {
            val dateTime = DateTimeUtils.getDate(instant)
            val monthName = dateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }

            return TransactionsState(
                selectedPeriod = instant,
                selectedPeriodReadable = "$monthName ${dateTime.year}",
            )
        }
    }
}
