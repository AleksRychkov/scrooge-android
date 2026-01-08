package dev.aleksrychkov.scrooge.core.entity

import kotlinx.serialization.Serializable

@Serializable
data class TransferStateEntity(
    val current: State = State.None(),
) {
    @Serializable
    sealed interface State {
        companion object {
            private const val NONE = 0
            private const val IMPORTING = 1
            private const val IMPORTING_FAILED = 2
            private const val EXPORTING = 3
            private const val EXPORTING_FAILED = 4
            private const val EXPORTING_SUCCESS = 5
            private const val IMPORTING_SUCCESS = 6

            fun fromOrdinal(ordinal: Int, info: String?): State {
                return when (ordinal) {
                    NONE -> None(info = info)
                    IMPORTING -> Importing(info = info)
                    IMPORTING_FAILED -> ImportingFailed(info = info)
                    EXPORTING -> Exporting(info = info)
                    EXPORTING_FAILED -> ExportingFailed(info = info)
                    EXPORTING_SUCCESS -> ExportingSuccess(info = info)
                    IMPORTING_SUCCESS -> ImportingSuccess(info = info)
                    else -> error("Unknown ordinal: $ordinal")
                }
            }
        }

        val ordinal: Int
        val info: String?

        @Serializable
        data class None(
            override val ordinal: Int = NONE,
            override val info: String? = null,
        ) : State

        @Serializable
        data class Importing(
            override val ordinal: Int = IMPORTING,
            override val info: String? = null,
        ) : State

        @Serializable
        data class ImportingSuccess(
            override val ordinal: Int = IMPORTING_SUCCESS,
            override val info: String? = null,
        ) : State

        @Serializable
        data class ImportingFailed(
            override val ordinal: Int = IMPORTING_FAILED,
            override val info: String? = null,
        ) : State

        @Serializable
        data class Exporting(
            override val ordinal: Int = EXPORTING,
            override val info: String? = null,
        ) : State

        @Serializable
        data class ExportingFailed(
            override val ordinal: Int = EXPORTING_FAILED,
            override val info: String? = null,
        ) : State

        @Serializable
        data class ExportingSuccess(
            override val ordinal: Int = EXPORTING_SUCCESS,
            override val info: String? = null,
        ) : State
    }
}
