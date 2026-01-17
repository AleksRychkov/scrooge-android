package dev.aleksrychkov.scrooge.presentation.screen.hub.internal

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.FilterEntity

@Immutable
internal data class HubState(
    val filter: FilterEntity = FilterEntity(),
)
