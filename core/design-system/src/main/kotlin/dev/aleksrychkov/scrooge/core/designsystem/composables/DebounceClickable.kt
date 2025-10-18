package dev.aleksrychkov.scrooge.core.designsystem.composables

import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

inline fun Modifier.debounceClickable(
    debounceInterval: Duration = 500.milliseconds,
    crossinline onClick: () -> Unit,
): Modifier = composed {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    clickable {
        val currentTime = System.currentTimeMillis()
        if ((currentTime - lastClickTime) < debounceInterval.inWholeMilliseconds) return@clickable
        lastClickTime = currentTime
        onClick()
    }
}
