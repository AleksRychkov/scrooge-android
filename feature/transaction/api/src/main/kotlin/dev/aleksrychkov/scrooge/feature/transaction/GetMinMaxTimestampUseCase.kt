package dev.aleksrychkov.scrooge.feature.transaction

import dev.aleksrychkov.scrooge.core.entity.PeriodDatestampEntity

fun interface GetMinMaxTimestampUseCase {
    suspend operator fun invoke(): PeriodDatestampEntity?
}
