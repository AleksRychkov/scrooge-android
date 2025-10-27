package dev.aleksrychkov.scrooge.feature.tag.internal

import dev.aleksrychkov.scrooge.core.database.TagDao
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.feature.tag.RestoreTagUseCaseResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DefaultRestoreTagUseCaseTest {
    private val dispatcher = StandardTestDispatcher()
    private val tagDao: TagDao = mockk(relaxed = true)
    private val useCase = DefaultRestoreTagUseCase(lazy { tagDao }, dispatcher)

    private val tag = TagEntity.from(name = "Travel").copy(id = 42L)

    @Test
    fun `When tagDao restore succeeds Then returns Success`() = runTest(dispatcher) {
        // Given
        coEvery { tagDao.restore(tag.id) } returns Unit
        // When
        val result = useCase(tag)
        // Then
        assertEquals(RestoreTagUseCaseResult.Success, result)
        coVerify(exactly = 1) { tagDao.restore(tag.id) }
    }

    @Test
    fun `When tagDao restore throws exception Then returns Failure`() = runTest(dispatcher) {
        // Given
        coEvery { tagDao.restore(tag.id) } throws RuntimeException("DB error")
        // When
        val result = useCase(tag)
        // Then
        assertEquals(RestoreTagUseCaseResult.Failure, result)
        coVerify(exactly = 1) { tagDao.restore(tag.id) }
    }
}
