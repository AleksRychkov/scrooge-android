package dev.aleksrychkov.scrooge.feature.tag

import dev.aleksrychkov.scrooge.core.entity.TagEntity

fun interface RestoreTagUseCase {
    suspend operator fun invoke(tag: TagEntity): RestoreTagUseCaseResult
}

sealed interface RestoreTagUseCaseResult {
    data object Success : RestoreTagUseCaseResult
    data object Failure : RestoreTagUseCaseResult
}
