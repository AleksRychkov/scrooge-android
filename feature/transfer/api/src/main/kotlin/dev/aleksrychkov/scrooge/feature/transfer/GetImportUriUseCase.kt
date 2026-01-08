package dev.aleksrychkov.scrooge.feature.transfer

fun interface GetImportUriUseCase {
    suspend operator fun invoke(): String?
}
