package dev.aleksrychkov.scrooge.feature.tag.internal

import dev.aleksrychkov.scrooge.common.database.TagDao
import dev.aleksrychkov.scrooge.common.entity.TagEntity
import dev.aleksrychkov.scrooge.feature.tag.CreateTagResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DefaultCreateTagUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private val tagDao = mockk<TagDao>(relaxed = true)
    private val useCase = DefaultCreateTagUseCase(lazy { tagDao }, testDispatcher)

    @Test
    fun `When tag does not exist Then Success result is returned`() = runTest(testDispatcher) {
        // Given
        val tag = TagEntity(id = 0L, name = "Groceries", colorHex = "#FF5733")
        coEvery { tagDao.getByName(tag.name) } returns null
        // When
        val result: Result<CreateTagResult> = useCase(tag)
        // Then
        assertEquals(Result.success(CreateTagResult.Success), result)
        coVerify(exactly = 1) { tagDao.create(tag.name, tag.colorHex) }
    }

    @Test
    fun `When tag with same name exists Then DuplicateViolation is returned`() =
        runTest(testDispatcher) {
            // Given
            val existing = TagEntity(id = 1L, name = "Groceries", colorHex = "#FF5733")
            val newTag = TagEntity(id = 2L, name = "Groceries", colorHex = "#00FF00")
            coEvery { tagDao.getByName(newTag.name) } returns existing

            // When
            val result: Result<CreateTagResult> = useCase(newTag)

            // Then
            assertEquals(Result.success(CreateTagResult.DuplicateViolation(existing)), result)
        }

    @Test
    fun `When dao throws exception Then failure result is returned`() = runTest(testDispatcher) {
        // Given
        val tag = TagEntity(id = 0L, name = "Groceries", colorHex = "#FF5733")
        coEvery { tagDao.getByName(tag.name) } throws IllegalStateException("DB error")
        // When
        val result: Result<CreateTagResult> = useCase(tag)
        // Then
        assert(result.isFailure)
    }
}
