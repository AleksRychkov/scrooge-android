package dev.aleksrychkov.scrooge.core.database.internal.mapper

import dev.aleksrychkov.scrooge.core.entity.TagEntity

internal object TagMapper {
    fun toEntity(id: Long, name: String): TagEntity = TagEntity(id = id, name = name)
}
