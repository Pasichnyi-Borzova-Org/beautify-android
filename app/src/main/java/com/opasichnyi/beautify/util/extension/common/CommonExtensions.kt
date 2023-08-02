package com.opasichnyi.beautify.util.extension.common

import androidx.annotation.CheckResult
import timber.log.Timber

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
    log: (Throwable?) -> Unit = Timber.Forest::w,
    block: () -> T,
): T? = runCatching(block).onFailure(log).getOrNull()
