package dev.aleksrychkov.scrooge.presentation.screen.root.internal

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import dev.aleksrychkov.scrooge.presentation.screen.root.R
import dev.aleksrychkov.scrooge.core.resources.R as Resources

internal fun installAddIncomeShortcut(context: Context) {
    val deeplink = context.getString(R.string.deeplink_add_income)
    val shortcut = ShortcutInfoCompat.Builder(context, deeplink)
        .setShortLabel(context.getString(Resources.string.add_income))
        .setIcon(
            IconCompat.createWithResource(
                context,
                R.drawable.ic_add_circle_24,
            )
        )
        .setIntent(Intent(Intent.ACTION_VIEW, deeplink.toUri()))
        .build()
    ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)
}

internal fun installAddExpenseShortcut(context: Context) {
    val deeplink = context.getString(R.string.deeplink_add_expense)
    val shortcut = ShortcutInfoCompat.Builder(context, deeplink)
        .setShortLabel(context.getString(Resources.string.add_expense))
        .setIcon(
            IconCompat.createWithResource(
                context,
                R.drawable.ic_remove_circle_24,
            )
        )
        .setIntent(Intent(Intent.ACTION_VIEW, deeplink.toUri()))
        .build()
    ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)
}
