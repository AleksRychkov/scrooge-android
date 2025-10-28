package dev.aleksrychkov.scrooge.component.currency.internal.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class CurrencyState(
    val currencies: ImmutableList<CurrencyEntity> = persistentListOf(),
    val filtered: ImmutableList<CurrencyEntity> = persistentListOf(),
    val searchQuery: String = "",
)
