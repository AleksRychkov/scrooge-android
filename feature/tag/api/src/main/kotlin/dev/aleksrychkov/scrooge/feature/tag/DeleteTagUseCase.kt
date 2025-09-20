package dev.aleksrychkov.scrooge.feature.tag

import dev.aleksrychkov.scrooge.common.entity.TagEntity

fun interface DeleteTagUseCase {
    suspend operator fun invoke(tagEntity: TagEntity): Result<Unit>
}
