package dev.aleksrychkov.scrooge.core.database.internal.mapper

import dev.aleksrychkov.scrooge.core.database.Tag
import dev.aleksrychkov.scrooge.core.entity.TagEntity

internal object TagMapper {
    fun toEntity(tag: Tag): TagEntity =
        TagEntity(
            id = tag.id,
            name = tag.name,
            colorHex = tag.colorHex,
        )
}
