package dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.LimitEntity
import dev.aleksrychkov.scrooge.core.entity.amountToString
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Immutable
internal data class LimitsState(
    val isLoading: Boolean = true,
    val editable: ImmutableList<LimitDto> = persistentListOf(),
    val lastUsedCurrencyEntity: CurrencyEntity = CurrencyEntity.RUB,
)

@Immutable
internal data class LimitDto(
    val id: Long,
    val periodText: String,
    val amount: String,
    val currencySymbol: String,
    val currencyCode: String,
)

internal fun List<LimitEntity>.toState(resourceManager: ResourceManager): ImmutableList<LimitDto> =
    this
        .map { entity ->
            LimitDto(
                id = entity.id,
                periodText = entity.period.periodToText(resourceManager),
                amount = entity.amount.amountToString(),
                currencySymbol = entity.currency.currencySymbol,
                currencyCode = entity.currency.currencyCode,
            )
        }
        .toImmutableList()

internal fun LimitDto.toEntity(resourceManager: ResourceManager) = LimitEntity(
    id = this.id,
    currency = CurrencyEntity.fromCurrencyCode(this.currencyCode)!!,
    amount = 1,
    period = this.periodText.periodTextToPeriod(resourceManager),
)

internal fun String.periodTextToPeriod(
    resourceManager: ResourceManager,
): LimitEntity.Period = when (this) {
    resourceManager.getString(Resources.string.limits_daily) -> LimitEntity.Period.Daily
    resourceManager.getString(Resources.string.limits_weekly) -> LimitEntity.Period.Weekly
    resourceManager.getString(Resources.string.limits_monthly) -> LimitEntity.Period.Monthly
    else -> error("Unknown period: $this")
}

internal fun LimitEntity.Period.periodToText(
    resourceManager: ResourceManager,
): String = when (this) {
    LimitEntity.Period.Daily -> resourceManager.getString(Resources.string.limits_daily)
    LimitEntity.Period.Weekly -> resourceManager.getString(Resources.string.limits_weekly)
    LimitEntity.Period.Monthly -> resourceManager.getString(Resources.string.limits_monthly)
}
