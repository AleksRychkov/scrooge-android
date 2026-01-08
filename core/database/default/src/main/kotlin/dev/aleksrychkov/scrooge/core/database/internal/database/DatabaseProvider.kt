package dev.aleksrychkov.scrooge.core.database.internal.database

import dev.aleksrychkov.scrooge.core.database.Scrooge

internal interface DatabaseProvider {
    val scrooge: Scrooge
}
