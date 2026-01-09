package dev.aleksrychkov.scrooge.presentation.component.filters.internal.modal

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.FiltersComponentInternal
import dev.aleksrychkov.scrooge.presentation.component.tags.TagModal

@Composable
internal fun FiltersTagModal(
    component: FiltersComponentInternal,
) {
    val categorySlot = component.tagModal.subscribeAsState().value
    TagModal(
        slot = categorySlot,
        close = component::closeTagModal,
        select = component::addTag,
    )
}
