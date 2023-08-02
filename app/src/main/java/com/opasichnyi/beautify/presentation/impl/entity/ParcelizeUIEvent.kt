package com.opasichnyi.beautify.presentation.impl.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

/**
 * [UIEvent] with [Parcelable] result type
 * for [putNavigationResult][com.opasichnyi.beautify.util.extension.navigation.putNavigationResult]
 */
@Parcelize class ParcelizeUIEvent<T, E> private constructor(
    private val _data: T?,
    private val _error: E?,
    private val _handled: Boolean,
) : Parcelable,
    UIEvent<T>(
        _data = _data,
        _error = _error,
        _handled = _handled,
    ) where T : Parcelable, E : Throwable, E : Serializable {

    constructor () : this(_data = null, _error = null, _handled = false)
    private constructor (data: T) : this(_data = data, _error = null, _handled = false)
    private constructor (error: E) : this(_data = null, _error = error, _handled = false)

    companion object {

        /**
         * Create [ParcelizeUIEvent] with default generic error
         */
        @SuppressWarnings("FunctionName")
        fun <T : Parcelable> ParcelizeUIEvent(data: T) =
            ParcelizeUIEvent<T, Throwable>(data)

        /**
         * Create [ParcelizeUIEvent] with default generic result
         */
        @SuppressWarnings("FunctionName")
        @Suppress("unused")
        fun <E> ParcelizeUIEvent(error: E) where E : Throwable, E : Serializable =
            ParcelizeUIEvent<Parcelable, E>(error)
    }
}
