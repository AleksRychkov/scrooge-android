package dev.aleksrychkov.scrooge.feature.tag.internal

import dev.aleksrychkov.scrooge.core.database.TagDao
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.feature.tag.ObserveTagsUseCaseResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class DefaultObserveTagsUseCaseTest {

    private val testDispatcher: CoroutineDispatcher = StandardTestDispatcher()
    private val tagDao = mockk<TagDao>()
    private val useCase = DefaultObserveTagsUseCase(lazy { tagDao }, testDispatcher)

    @Test
    fun `When dao returns flow Then result is success`() = runTest(testDispatcher) {
        // Given
        val tags: ImmutableList<TagEntity> = persistentListOf(
            TagEntity(id = 1L, name = "Groceries", colorHex = "#FF5733")
        )
        val flow: Flow<ImmutableList<TagEntity>> = flowOf(tags)
        coEvery { tagDao.get() } returns flow
        // When
        val result: ObserveTagsUseCaseResult = useCase()
        // Then
        assertTrue(result is ObserveTagsUseCaseResult.Success)
        coVerify(exactly = 1) {
            @Suppress("UnusedFlow")
            tagDao.get()
        }
    }

    @Test
    fun `When dao throws exception Then result is failure`() = runTest(testDispatcher) {
        // Given
        coEvery { tagDao.get() } throws IllegalStateException("DB error")
        // When
        val result: ObserveTagsUseCaseResult = useCase()
        // Then
        assertEquals(ObserveTagsUseCaseResult.Failure, result)
        coVerify(exactly = 1) {
            @Suppress("UnusedFlow")
            tagDao.get()
        }
    }
}
