package dev.aleksrychkov.scrooge.core.resources.internal

import android.content.Context
import dev.aleksrychkov.scrooge.core.resources.ResourceManager

internal class DefaultResourceManager(
    private val context: Context,
) : ResourceManager {
    override fun getString(stringRes: Int): String =
        context.getString(stringRes)

    override fun getString(stringRes: Int, vararg args: String): String =
        context.getString(stringRes, *args)

    override fun getStringArray(id: Int): Array<String> =
        context.resources.getStringArray(id)
}
