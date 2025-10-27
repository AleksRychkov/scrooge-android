package dev.aleksrychkov.scrooge.component.tag.internal.udf.actors

import dev.aleksrychkov.scrooge.component.tag.internal.udf.TagCommand
import dev.aleksrychkov.scrooge.component.tag.internal.udf.TagEvent
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.feature.tag.CreateTagResult
import dev.aleksrychkov.scrooge.feature.tag.CreateTagUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CreateTagDelegateTest {
    private val useCase: CreateTagUseCase = mockk()
    private val delegate = CreateTagDelegate(lazy { useCase })

    private val cmd = TagCommand.CreateNewTag(name = "Coffee")
    private val entity = TagEntity.from(name = "Coffee")

    @Test
    fun `When name is blank Then result is FailedToCreateNewTagEmptyName`() = runTest {
        // Given
        // When
        val result = delegate(cmd.copy(name = "")).first()
        // Then
        assertEquals(TagEvent.Internal.FailedToCreateNewTagEmptyName, result)
    }

    @Test
    fun `When useCase returns DuplicateViolation Then result is FailedToCreateNewTagDuplicate`() = runTest {
        // Given
        coEvery { useCase(any()) } returns CreateTagResult.DuplicateViolation(entity)
        // When
        val result = delegate(cmd).first()
        // Then
        assertEquals(TagEvent.Internal.FailedToCreateNewTagDuplicate(entity), result)
    }

    @Test
    fun `When useCase returns Failure Then result is FailedToCreateNewTag`() = runTest {
        // Given
        coEvery { useCase(any()) } returns CreateTagResult.Failure
        // When
        val result = delegate(cmd).first()
        // Then
        assertEquals(TagEvent.Internal.FailedToCreateNewTag, result)
    }

    @Test
    fun `When useCase returns Success Then result is empty`() = runTest {
        // Given
        coEvery { useCase(any()) } returns CreateTagResult.Success
        // When
        val result = delegate(cmd).firstOrNull()
        // Then
        assertEquals(null, result)
    }
}
