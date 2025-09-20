package dev.aleksrychkov.scrooge.core.utils

abstract class NoStackTraceException(msg: String) : RuntimeException(msg) {
    override fun fillInStackTrace(): Throwable = this
}
