package dev.aleksrychkov.scrooge.component.tag.internal.udf

import dev.aleksrychkov.scrooge.core.entity.TagEntity

internal sealed interface TagEffect {
    data class ShowInfoMessage(val message: String) : TagEffect
    data class TagDeleted(
        val message: String,
        val actionLabel: String,
        val tag: TagEntity,
    ) : TagEffect
}
