package com.opasichnyi.beautify.util.extension.concurrent

import androidx.annotation.CheckResult
import com.opasichnyi.beautify.BuildConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber

/**
 * @see CoroutineExceptionHandler
 */
@get:CheckResult val safeCoroutineExceptionHandler
    get(): CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            if (BuildConfig.DEBUG) {
                Thread.currentThread()
                    .uncaughtExceptionHandler
                    ?.uncaughtException(Thread.currentThread(), throwable)
            } else {
                Timber.e(throwable, "Unexpected error")
            }
        }

/**
 * Suspend calling coroutine and execute [block] on cancellation.
 *
 * @see CancellationException
 *
 * @param block lambda to execute on coroutine cancellation.
 */
@Suppress("TooGenericExceptionCaught")
suspend inline fun awaitCancellation(block: () -> Unit) {
    try {
        kotlinx.coroutines.awaitCancellation()
    } catch (_: CancellationException) {
        block()
    } catch (throwable: Throwable) {
        Timber.e(throwable)
    }
}
