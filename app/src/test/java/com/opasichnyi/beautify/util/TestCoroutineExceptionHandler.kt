package com.opasichnyi.beautify.util

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

class TestCoroutineExceptionHandler : CoroutineExceptionHandler {

    override val key: CoroutineContext.Key<*> = CoroutineExceptionHandler

    private val _exceptions = mutableListOf<Throwable>()

    val uncaughtExceptions: List<Throwable>
        get() = synchronized(this) { _exceptions.toList() }

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        synchronized(this) {
            _exceptions.add(exception)
        }
    }
}
