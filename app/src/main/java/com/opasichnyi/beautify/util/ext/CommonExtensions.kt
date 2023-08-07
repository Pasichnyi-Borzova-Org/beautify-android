package com.opasichnyi.beautify.util.ext

import androidx.annotation.CheckResult
import java.io.Closeable

/**
 * @see lazy
 * @see LazyThreadSafetyMode.SYNCHRONIZED
 */
@CheckResult fun <T> safeLazy(initializer: () -> T): Lazy<T> =
    lazy(LazyThreadSafetyMode.SYNCHRONIZED, initializer)

/**
 * @see lazy
 * @see LazyThreadSafetyMode.PUBLICATION
 */
@CheckResult fun <T> safeInitLazy(initializer: () -> T): Lazy<T> =
    lazy(LazyThreadSafetyMode.PUBLICATION, initializer)

/**
 * @see lazy
 * @see LazyThreadSafetyMode.NONE
 */
@CheckResult fun <T> unsafeLazy(initializer: () -> T): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE, initializer)

/**
 * Tries to execute [block] and return it's result.
 *
 * @param log function that will log all thrown exceptions.
 * @param block function to execute and return result.
 *
 * @return result of [block], if exception was thrown - return null.
 */
@CheckResult inline fun <T> tryOrNull(
    log: (Throwable) -> Unit,
    block: () -> T,
): T? = runCatching(block).onFailure(log).getOrNull()

/**
 * "Try-with-resources" for Kotlin.
 * Usage:
 * ```kotlin
 * arrayOf(inputStream, outputStream).use {
 *     // do something with the streams
 *     outputStream.write(inputStream.read())
 * }
 * ```
 */
@Throws(Throwable::class)
inline fun <T : Closeable?, R> Array<T>.use(block: () -> R): R {
    var exception: Throwable? = null
    try {
        return block()
    } catch (e: Throwable) {
        exception = e
        throw e
    } finally {
        when (exception) {
            null -> forEach { it?.close() }
            else -> forEach {
                try {
                    it?.close()
                } catch (closeException: Throwable) {
                    exception.addSuppressed(closeException)
                }
            }
        }
    }
}