package dev.aleksrychkov.scrooge.presentation.screen.transaction.internal

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.FilterEntity

@Immutable
internal data class TransactionsState(
    val filter: FilterEntity = FilterEntity(),
)
