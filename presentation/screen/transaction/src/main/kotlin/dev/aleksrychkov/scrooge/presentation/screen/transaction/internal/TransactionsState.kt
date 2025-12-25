package dev.aleksrychkov.scrooge.presentation.screen.transaction.internal

import androidx.compose.runtime.Immutable
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

@Immutable
internal data class TransactionsState(
    val selectedPeriod: Instant,
    val selectedPeriodReadable: String,
) {
    companion object {
        operator fun invoke(instant: Instant = Clock.System.now()): TransactionsState {
            val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
            val monthName = dateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }

            return TransactionsState(
                selectedPeriod = instant,
                selectedPeriodReadable = "$monthName ${dateTime.year}",
            )
        }
    }
}
