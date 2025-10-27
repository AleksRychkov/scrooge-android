package dev.aleksrychkov.scrooge.component.tag.internal.udf.actors

import dev.aleksrychkov.scrooge.component.tag.internal.udf.TagEvent
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.feature.tag.ObserveTagsUseCase
import dev.aleksrychkov.scrooge.feature.tag.ObserveTagsUseCaseResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ObserveTagsDelegateTest {

    private val useCase: ObserveTagsUseCase = mockk()
    private val delegate = ObserveTagsDelegate(lazy { useCase })

    private val tags = persistentListOf(
        TagEntity.from(name = "Coffee"),
        TagEntity.from(name = "Tea"),
    )

    @Test
    fun `When useCase returns Failure Then result is FailedToObserveTags`() = runTest {
        // Given
        coEvery { useCase() } returns ObserveTagsUseCaseResult.Failure
        // When
        val result = delegate().toList()
        // Then
        assertEquals(listOf(TagEvent.Internal.FailedToObserveTags), result)
    }

    @Test
    fun `When useCase returns Success Then emits Tags events`() = runTest {
        // Given
        coEvery { useCase() } returns ObserveTagsUseCaseResult.Success(flowOf(tags))
        // When
        val result = delegate().toList()
        // Then
        assertEquals(listOf(TagEvent.Internal.Tags(tags)), result)
    }
}
