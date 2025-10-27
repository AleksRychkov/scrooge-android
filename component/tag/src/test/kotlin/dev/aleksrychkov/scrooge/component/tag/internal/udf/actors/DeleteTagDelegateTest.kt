package dev.aleksrychkov.scrooge.component.tag.internal.udf.actors

import dev.aleksrychkov.scrooge.component.tag.internal.udf.TagCommand
import dev.aleksrychkov.scrooge.component.tag.internal.udf.TagEvent
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.feature.tag.DeleteTagResult
import dev.aleksrychkov.scrooge.feature.tag.DeleteTagUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DeleteTagDelegateTest {

    private val useCase: DeleteTagUseCase = mockk()
    private val delegate = DeleteTagDelegate(lazy { useCase })

    private val tag = TagEntity.from(name = "Coffee")
    private val cmd = TagCommand.Delete(tag = tag)

    @Test
    fun `When useCase returns Failure Then result is FailedToDeleteTag`() = runTest {
        // Given
        coEvery { useCase(any()) } returns DeleteTagResult.Failure
        // When
        val result = delegate(cmd).first()
        // Then
        assertEquals(TagEvent.Internal.FailedToDeleteTag, result)
    }

    @Test
    fun `When useCase returns Success Then result is DeletedTag`() = runTest {
        // Given
        coEvery { useCase(any()) } returns DeleteTagResult.Success
        // When
        val result = delegate(cmd).first()
        // Then
        assertEquals(TagEvent.Internal.DeletedTag(tag), result)
    }
}
