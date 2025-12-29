package dev.aleksrychkov.scrooge.core.entity

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.serialization.Serializable

@Serializable
data class FilterEntity(
    val period: PeriodDatestampEntity = PeriodDatestampEntity(Datestamp.ZERO, Datestamp.ZERO),
    val tags: ImmutableSet<String> = persistentSetOf(),
) {
    companion object {
        fun currentMonth(): FilterEntity {
            val today = Datestamp.now().date
            val period = startEndOfMonth(month = today.month, year = today.year)
            return FilterEntity(period = period)
        }

        fun currentYear(): FilterEntity {
            val today = Datestamp.now().date
            val period = startEndOfYear(year = today.year)
            return FilterEntity(period = period)
        }
    }
}
