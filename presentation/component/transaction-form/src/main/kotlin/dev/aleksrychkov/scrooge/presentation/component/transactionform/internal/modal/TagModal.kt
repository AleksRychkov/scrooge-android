package dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.modal

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.presentation.component.tags.TagSelectionModal
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.TransactionFormComponentInternal
import kotlinx.collections.immutable.ImmutableSet

@Composable
internal fun FormTagModal(
    component: TransactionFormComponentInternal,
    tags: ImmutableSet<TagEntity>,
    setTags: (Set<TagEntity>) -> Unit,
) {
    val slot = component.tagModal.subscribeAsState().value
    TagSelectionModal(
        slot = slot,
        close = component::closeTagModal,
        initialSelection = tags,
        callback = setTags,
    )
}
