package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf

import androidx.compose.runtime.Immutable

@Immutable
internal data class FiltersState(
    val fromDate: Date? = null,
    val toDate: Date? = null,
) {
    @Immutable
    data class Date(
        val day: Int,
        val month: Int,
        val year: Int,
    ) {
        val key: String
            get() = "$day.$month.$year"
    }
}
