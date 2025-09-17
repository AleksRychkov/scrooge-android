package dev.aleksrychkov.scrooge.common.entity

@JvmInline
value class CurrencyCode(val value: String) {
    override fun toString(): String {
        return value
    }
}

@JvmInline
value class CurrencySymbol(val value: String) {
    override fun toString(): String {
        return value
    }
}
