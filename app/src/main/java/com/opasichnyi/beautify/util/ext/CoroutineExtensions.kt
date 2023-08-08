package com.opasichnyi.beautify.util.ext

import androidx.annotation.CheckResult
import com.opasichnyi.beautify.BuildConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.CoroutineContext

/**
 * @see CoroutineExceptionHandler
 */
@CheckResult
fun getSafeCoroutineExceptionHandler(): CoroutineExceptionHandler =
    CoroutineExceptionHandler { _, throwable ->
        if (BuildConfig.DEBUG) {
            Thread.currentThread()
                .uncaughtExceptionHandler
                ?.uncaughtException(Thread.currentThread(), throwable)
        }
    }

/**
 * Suspend calling coroutine and execute [block] on cancellation.
 *
 * @see CancellationException
 *
 * @param block lambda to execute on coroutine cancellation.
 */
suspend inline fun awaitCancellation(block: () -> Unit) {
    try {
        kotlinx.coroutines.awaitCancellation()
    } catch (_: CancellationException) {
        block()
    } catch (_: Throwable) {
    }
}

inline fun CoroutineScope.ensureActive(crossinline onCancelled: () -> Unit) {
    coroutineContext.ensureActive(onCancelled)
}

inline fun CoroutineContext.ensureActive(crossinline onCancelled: () -> Unit) {
    get(Job)?.ensureActive(onCancelled)
}

inline fun Job.ensureActive(onCancelled: () -> Unit) {
    try {
        ensureActive()
    } catch (e: CancellationException) {
        onCancelled.invoke()
    }
}
