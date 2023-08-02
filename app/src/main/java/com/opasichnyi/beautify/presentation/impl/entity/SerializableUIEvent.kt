package com.opasichnyi.beautify.presentation.impl.entity

import android.os.Parcelable
import java.io.Serializable

/**
 * [UIEvent] with [Parcelable] result type
 * for [putNavigationResult][com.opasichnyi.beautify.util.extension.navigation.putNavigationResult]
 */
class SerializableUIEvent<T, E> private constructor(
    private val _data: T?,
    private val _error: E?,
    private val _handled: Boolean,
) : Serializable,
    UIEvent<T>(
        _data = _data,
        _error = _error,
        _handled = _handled,
    ) where T : Serializable, E : Throwable, E : Serializable {

    constructor () : this(_data = null, _error = null, _handled = false)
    private constructor (data: T) : this(_data = data, _error = null, _handled = false)
    private constructor (error: E) : this(_data = null, _error = error, _handled = false)

    companion object {

        /**
         * https://detekt.dev/docs/rules/style/#serialversionuidinserializableclass
         */
        @SuppressWarnings("PropertyName")
        private const val serialVersionUID = 1L

        /**
         * Create [SerializableUIEvent] with default generic error
         */
        @SuppressWarnings("FunctionName")
        fun <T : Serializable> SerializableUIEvent(data: T) =
            SerializableUIEvent<T, Throwable>(data)

        /**
         * Create [SerializableUIEvent] with default generic result
         */
        @SuppressWarnings("FunctionName")
        @Suppress("unused")
        fun <E> SerializableUIEvent(error: E) where E : Throwable, E : Serializable =
            SerializableUIEvent<Serializable, E>(error)
    }
}
