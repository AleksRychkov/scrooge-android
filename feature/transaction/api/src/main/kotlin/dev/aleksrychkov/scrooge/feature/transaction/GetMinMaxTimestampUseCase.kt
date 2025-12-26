package dev.aleksrychkov.scrooge.feature.transaction

import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity

fun interface GetMinMaxTimestampUseCase {
    suspend operator fun invoke(): PeriodTimestampEntity?
}
