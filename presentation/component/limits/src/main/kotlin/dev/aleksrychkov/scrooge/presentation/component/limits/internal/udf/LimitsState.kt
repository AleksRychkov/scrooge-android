package dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.LimitDataEntity
import dev.aleksrychkov.scrooge.core.entity.LimitEntity
import dev.aleksrychkov.scrooge.core.entity.amountToString
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.math.abs
import dev.aleksrychkov.scrooge.core.resources.R as Resources

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

internal fun LimitDataEntity.toLimitProgress(resourceManager: ResourceManager): LimitProgress {
    val period = when (this.limit.period) {
        LimitEntity.Period.Daily -> "\uD83D\uDCC5 " + resourceManager.getString(Resources.string.limits_daily)
        LimitEntity.Period.Weekly -> "\uD83D\uDCC6 " + resourceManager.getString(Resources.string.limits_weekly)
        LimitEntity.Period.Monthly -> "\uD83D\uDDD3 " + resourceManager.getString(Resources.string.limits_monthly)
    }
    val progress = this.spentAmount / this.limit.amount.toFloat()
    val totalInfoPrefix = abs(this.limit.amount - this.spentAmount).amountToString()
    val totalInfoSuffix = if (this.limit.amount - this.spentAmount < 0) {
        resourceManager.getString(Resources.string.limits_over)
    } else {
        resourceManager.getString(Resources.string.limits_left)
    }
    return LimitProgress(
        period = period,
        progress = minOf(1f, progress),
        overflowProgress = maxOf(0f, progress - 1f),
        totalInfo = "$totalInfoPrefix ${this.limit.currency.currencySymbol} $totalInfoSuffix"
    )
}
