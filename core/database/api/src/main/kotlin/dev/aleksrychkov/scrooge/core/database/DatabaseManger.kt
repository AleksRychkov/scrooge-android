package dev.aleksrychkov.scrooge.core.database

interface DatabaseManger {
    suspend fun close()
    suspend fun open()
    suspend fun cleanup()
}
