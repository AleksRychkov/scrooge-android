package dev.aleksrychkov.scrooge.component.tag.internal.udf.actors

import dev.aleksrychkov.scrooge.component.tag.internal.udf.TagCommand
import dev.aleksrychkov.scrooge.component.tag.internal.udf.TagEvent
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class SearchTagDelegateTest {

    private val delegate = SearchTagsDelegate()

    private val tags = listOf(
        TagEntity.from(name = "Food"),
        TagEntity.from(name = "Transport"),
        TagEntity.from(name = "Football")
    )

    @Test
    fun `When query is blank Then result is empty list`() = runTest {
        // Given
        val cmd = TagCommand.Search(query = "", tags = tags)
        // When
        val result = delegate(cmd).first()
        // Then
        assertEquals(TagEvent.Internal.Filtered(list = persistentListOf()), result)
    }

    @Test
    fun `When query matches some tags Then only matching tags are returned`() = runTest {
        // Given
        val cmd = TagCommand.Search(query = "foo", tags = tags)
        // When
        val result = delegate(cmd).first()
        // Then
        val expectedFiltered = tags.filter { it.name.lowercase().contains("foo") }
        assertEquals(TagEvent.Internal.Filtered(list = expectedFiltered.toImmutableList()), result)
    }

    @Test
    fun `When query matches no tags Then result is empty list`() = runTest {
        // Given
        val cmd = TagCommand.Search(query = "travel", tags = tags)
        // When
        val result = delegate(cmd).first()
        // Then
        assertEquals(TagEvent.Internal.Filtered(list = persistentListOf()), result)
    }
}
