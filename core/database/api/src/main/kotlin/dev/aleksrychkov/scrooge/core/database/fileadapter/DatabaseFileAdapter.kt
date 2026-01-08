package dev.aleksrychkov.scrooge.core.database.fileadapter

import java.io.DataInput
import java.io.DataOutput

interface DatabaseFileAdapter {
    suspend fun write(output: DataOutput)
    suspend fun read(input: DataInput)
}
