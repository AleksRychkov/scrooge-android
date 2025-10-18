package dev.aleksrychkov.scrooge.core.resources

import androidx.annotation.StringRes

interface ResourceManager {
    fun getString(@StringRes stringRes: Int): String
    fun getString(@StringRes stringRes: Int, vararg args: String): String
}
