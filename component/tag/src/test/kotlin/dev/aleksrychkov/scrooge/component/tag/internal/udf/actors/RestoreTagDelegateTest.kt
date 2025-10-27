package dev.aleksrychkov.scrooge.component.tag.internal.udf.actors

import dev.aleksrychkov.scrooge.component.tag.internal.udf.TagCommand
import dev.aleksrychkov.scrooge.component.tag.internal.udf.TagEvent
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.feature.tag.RestoreTagUseCase
import dev.aleksrychkov.scrooge.feature.tag.RestoreTagUseCaseResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

internal class RestoreTagDelegateTest {
    private val useCase: RestoreTagUseCase = mockk()
    private val delegate: RestoreTagDelegate = RestoreTagDelegate(lazy { useCase })

    private val entity = TagEntity(
        id = 1L,
        name = "Coffee",
    )
    private val cmd = TagCommand.Restore(tag = entity)

    @Test
    fun `When useCase returns Success Then result is empty flow`() = runTest {
        // Given
        coEvery { useCase(entity) } returns RestoreTagUseCaseResult.Success
        // When
        val result = delegate(cmd).firstOrNull()
        // Then
        assertNull(result)
    }

    @Test
    fun `When useCase returns Failure Then result is FailedToRestoreTag`() = runTest {
        // Given
        coEvery { useCase(entity) } returns RestoreTagUseCaseResult.Failure
        // When
        val result = delegate(cmd).first()
        // Then
        assertEquals(TagEvent.Internal.FailedToRestoreTag, result)
    }
}
