package dev.aleksrychkov.scrooge.core.router

interface Router {
    fun open(destination: Destination) {
        throw NotImplementedError()
    }

    fun close() {
        throw NotImplementedError()
    }
}
