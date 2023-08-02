package com.opasichnyi.beautify.presentation.impl.entity

/**
 * A wrapper that allows you to handle one-time events in the View from the Presentation layer.
 * This wrapper keeps a cache of published data, which you can get using the "force" parameter.
 *
 * Inspiration https://developer.android.com/topic/architecture/ui-layer/events
 */
open class UIEvent<T> protected constructor(
    private val _data: T?,
    private val _error: Throwable?,
    private var _handled: Boolean,
) {

    /**
     * Was call [getDataOrNull] or [getErrorOrNull] or not
     */
    val handled: Boolean get() = _handled

    val isEmptyState: Boolean get() = _data == null && _error == null

    constructor() : this(_data = null, _error = null, _handled = false)
    constructor(data: T) : this(_data = data, _error = null, _handled = false)
    constructor(error: Throwable) : this(_data = null, _error = error, _handled = false)

    fun getDataOrNull(forceResult: Boolean = false): T? = get(_data, forceResult)
    fun getErrorOrNull(forceResult: Boolean = false): Throwable? = get(_error, forceResult)

    /**
     * Get [Result] and set handle [handled] if need.
     * You can ignore [handled] if [forceResult] is true (only for subsequent after first).
     */
    private fun <Result> get(k: Result?, forceResult: Boolean): Result? = k.apply {
        when {
            isEmptyState -> _handled = true
            k == null -> return@get null
            !_handled -> _handled = true
            !forceResult -> return@get null
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UIEvent<*>

        if (_data != other._data) return false
        if (_error != other._error) return false
        if (_handled != other._handled) return false

        return true
    }

    override fun hashCode(): Int {
        var result = _data?.hashCode() ?: 0
        result = 31 * result + (_error?.hashCode() ?: 0)
        return result
    }
}
