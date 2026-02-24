package dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.LimitEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class LimitsState(
    val isVisible: Boolean = false,
    val limits: ImmutableList<LimitProgress> = persistentListOf(
        LimitProgress(
            period = "\uD83D\uDCC5 " + LimitEntity.Period.Daily.name,
            progress = 1.0f,
            overflowProgress = 0.25f,
            totalInfo = "250₽ over",
        )
    )
)

@Immutable
internal data class LimitProgress(
    val period: String = "",
    val progress: Float = 0.0f,
    val overflowProgress: Float = 0.0f,
    val totalInfo: String = "",
)
