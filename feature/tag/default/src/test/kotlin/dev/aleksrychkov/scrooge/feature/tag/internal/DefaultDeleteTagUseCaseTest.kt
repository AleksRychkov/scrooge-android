package dev.aleksrychkov.scrooge.feature.tag.internal

import dev.aleksrychkov.scrooge.core.database.TagDao
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.feature.tag.DeleteTagResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DefaultDeleteTagUseCaseTest {
    private val testDispatcher = StandardTestDispatcher()
    private val tagDao = mockk<TagDao>(relaxed = true)
    private val useCase = DefaultDeleteTagUseCase(lazy { tagDao }, testDispatcher)

    @Test
    fun `When delete succeeds Then result is success`() = runTest(testDispatcher) {
        // Given
        val tag = TagEntity(id = 1L, name = "Groceries", colorHex = "#FF5733")
        coEvery { tagDao.delete(tag.id) } returns Unit
        // When
        val result: DeleteTagResult = useCase(tag)
        // Then
        assertEquals(DeleteTagResult.Success, result)
        coVerify(exactly = 1) { tagDao.delete(tag.id) }
    }

    @Test
    fun `When dao throws exception Then result is failure`() = runTest(testDispatcher) {
        // Given
        val tag = TagEntity(id = 2L, name = "Food", colorHex = "#00FF00")
        coEvery { tagDao.delete(tag.id) } throws IllegalStateException("DB error")
        // When
        val result: DeleteTagResult = useCase(tag)
        // Then
        assertEquals(DeleteTagResult.Failure, result)
        coVerify(exactly = 1) { tagDao.delete(tag.id) }
    }
}
