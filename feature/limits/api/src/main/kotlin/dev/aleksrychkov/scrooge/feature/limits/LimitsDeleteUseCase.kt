package dev.aleksrychkov.scrooge.feature.limits

fun interface LimitsDeleteUseCase {
    suspend operator fun invoke(id: Long): LimitsDeleteResult
}

sealed interface LimitsDeleteResult {
    data object Success : LimitsDeleteResult
    data object Failure : LimitsDeleteResult
}
