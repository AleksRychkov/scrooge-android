package dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.LimitDataEntity
import dev.aleksrychkov.scrooge.core.entity.LimitEntity
import dev.aleksrychkov.scrooge.core.entity.amountToStringFormatted
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Immutable
internal data class LimitsState(
    val isVisible: Boolean = false,
    val limits: ImmutableList<LimitProgress> = persistentListOf(),
)

@Immutable
internal data class LimitProgress(
    val period: String = "",
    val progress: Float = 0.0f,
    val overflowProgress: Float = 0.0f,
    val limit: String = "",
    val spent: String = "",
    val currencySymbol: String = "",
)

internal fun LimitDataEntity.toLimitProgress(resourceManager: ResourceManager): LimitProgress {
    val period = when (this.limit.period) {
        LimitEntity.Period.Daily -> "\uD83D\uDCC5 " + resourceManager.getString(Resources.string.limits_daily)
        LimitEntity.Period.Weekly -> "\uD83D\uDCC6 " + resourceManager.getString(Resources.string.limits_weekly)
        LimitEntity.Period.Monthly -> "\uD83D\uDDD3 " + resourceManager.getString(Resources.string.limits_monthly)
    }
    val totalRatio = this.spentAmount / this.limit.amount.toFloat()

    val progress: Float
    val overflowProgress: Float

    if (totalRatio <= 1f) {
        progress = totalRatio
        overflowProgress = 0f
    } else {
        progress = 1f / totalRatio
        overflowProgress = 1f - progress
    }

    return LimitProgress(
        period = period,
        progress = progress,
        overflowProgress = overflowProgress,
        limit = this.limit.amount.amountToStringFormatted(""),
        spent = this.spentAmount.amountToStringFormatted(""),
        currencySymbol = this.limit.currency.currencySymbol
    )
}
