package dev.aleksrychkov.scrooge.common.database.internal.mapper

import dev.aleksrychkov.scrooge.common.database.Tag
import dev.aleksrychkov.scrooge.common.entity.TagEntity

internal object TagMapper {
    fun toEntity(tag: Tag): TagEntity =
        TagEntity(
            id = tag.id,
            name = tag.name,
            colorHex = tag.colorHex,
        )
}
