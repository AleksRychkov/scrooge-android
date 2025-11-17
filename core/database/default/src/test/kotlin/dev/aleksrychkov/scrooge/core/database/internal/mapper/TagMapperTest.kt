package dev.aleksrychkov.scrooge.core.database.internal.mapper

import dev.aleksrychkov.scrooge.core.database.Tag
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class TagMapperTest {

    @Test
    fun `When tag mapped to entity Then all fields are converted correctly`() {
        // Given
        val tag = Tag(
            id = 1L,
            name = "Groceries",
            colorHex = "#FF5733",
            isDeleted = 0L,
        )
        // When
        val entity: TagEntity = TagMapper.toEntity(tag)
        // Then
        assertEquals(1L, entity.id)
        assertEquals("Groceries", entity.name)
        assertEquals("#FF5733", entity.colorHex)
    }

    @Test
    fun `When colorHex is empty string Then entity keeps empty colorHex`() {
        // Given
        val tag = Tag(
            id = 2L,
            name = "NoColor",
            colorHex = "",
            isDeleted = 0L,
        )
        // When
        val entity = TagMapper.toEntity(tag)
        // Then
        assertEquals("", entity.colorHex)
    }

    @Test
    fun `When colorHex is null Then entity has null colorHex`() {
        // Given
        val tag = Tag(
            id = 3L,
            name = "OptionalColor",
            colorHex = null,
            isDeleted = 0L,
        )
        // When
        val entity = TagMapper.toEntity(tag)
        // Then
        assertEquals(null, entity.colorHex)
    }
}
