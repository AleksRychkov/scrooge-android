package dev.aleksrychkov.scrooge.presentaion.component.transactioncurrency.internal.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.presentaion.component.transactioncurrency.internal.entity.FavoriteCurrencyEntity
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal data class CurrencyState(
    val all: ImmutableList<FavoriteCurrencyEntity>? = null,
    val favorite: ImmutableList<FavoriteCurrencyEntity>? = null,
)
