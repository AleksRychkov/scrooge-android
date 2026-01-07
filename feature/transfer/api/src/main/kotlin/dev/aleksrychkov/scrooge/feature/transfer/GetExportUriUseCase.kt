package dev.aleksrychkov.scrooge.feature.transfer

fun interface GetExportUriUseCase {
    suspend operator fun invoke(fileName: String): String?
}
