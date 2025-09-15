package dev.aleksrychkov.scrooge.core.di

private object UNINITIALIZED

internal abstract class NaiveProvider<T>(
    private val definition: NaiveDefinition<T>,
) {
    protected fun instantiate(): T = definition.invoke(Naive)
    abstract fun get(): T
}

internal class NaiveFactoryProvider<T>(
    definition: NaiveDefinition<T>
) : NaiveProvider<T>(definition) {
    override fun get(): T = instantiate()
}

internal class NaiveSingletonProvider<T>(
    definition: NaiveDefinition<T>
) : NaiveProvider<T>(definition) {

    @Volatile
    private var instance: Any? = UNINITIALIZED

    private val lock = Any()

    override fun get(): T {
        val value1 = instance
        if (value1 !== UNINITIALIZED) {
            @Suppress("UNCHECKED_CAST")
            return value1 as T
        }

        return synchronized(lock) {
            val value2 = instance
            if (value2 !== UNINITIALIZED) {
                @Suppress("UNCHECKED_CAST")
                value2 as T
            } else {
                val typedValue = instantiate()
                instance = typedValue
                typedValue
            }
        }
    }
}
