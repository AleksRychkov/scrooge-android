package dev.aleksrychkov.scrooge.feature.tag

import dev.aleksrychkov.scrooge.core.entity.TagEntity

fun interface CreateTagUseCase {
    suspend operator fun invoke(tagEntity: TagEntity): CreateTagResult
}

sealed interface CreateTagResult {
    data object Success : CreateTagResult
    data class DuplicateViolation(val tag: TagEntity) : CreateTagResult
    data object Failure : CreateTagResult
}
