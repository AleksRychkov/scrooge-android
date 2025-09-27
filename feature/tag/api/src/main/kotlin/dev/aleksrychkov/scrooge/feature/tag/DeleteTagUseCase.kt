package dev.aleksrychkov.scrooge.feature.tag

import dev.aleksrychkov.scrooge.core.entity.TagEntity

fun interface DeleteTagUseCase {
    suspend operator fun invoke(tagEntity: TagEntity): Result<Unit>
}
