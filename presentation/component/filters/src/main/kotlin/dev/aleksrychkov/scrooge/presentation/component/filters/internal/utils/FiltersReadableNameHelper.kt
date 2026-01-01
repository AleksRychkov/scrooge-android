package dev.aleksrychkov.scrooge.presentation.component.filters.internal.utils

import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.readableName
import dev.aleksrychkov.scrooge.core.resources.R
import dev.aleksrychkov.scrooge.core.resources.ResourceManager

internal class FiltersReadableNameHelper(
    private val resourceManager: ResourceManager,
) {
    private var _months: Array<String>? = null
    private var _shortMonths: Array<String>? = null

    fun getName(filter: FilterEntity): String {
        val months = getMonths()
        val shortMonths = getShortMonths()
        return filter.readableName(months = months, shortMonths = shortMonths)
    }

    private fun getMonths(): Array<String> {
        if (_months == null) {
            _months = resourceManager.getStringArray(R.array.month_names)
        }
        return _months!!
    }

    private fun getShortMonths(): Array<String> {
        if (_shortMonths == null) {
            _shortMonths = resourceManager.getStringArray(R.array.short_month_names)
        }
        return _shortMonths!!
    }
}
