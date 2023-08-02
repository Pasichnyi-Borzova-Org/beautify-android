package com.opasichnyi.beautify.util.extension.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

/**
 * @see Lifecycle.launchOnLifecycleCreate
 */
inline fun LifecycleOwner.launchOnLifecycleCreate(
    crossinline block: suspend CoroutineScope.() -> Unit,
): Job = lifecycle.launchOnLifecycleCreate { block() }

/**
 * @see Lifecycle.launchOnLifecycleStart
 */
inline fun LifecycleOwner.launchOnLifecycleStart(
    crossinline block: suspend CoroutineScope.() -> Unit,
): Job = lifecycle.launchOnLifecycleStart { block() }

/**
 * @see Lifecycle.launchOnLifecycleResume
 */
inline fun LifecycleOwner.launchOnLifecycleResume(
    crossinline block: suspend CoroutineScope.() -> Unit,
): Job = lifecycle.launchOnLifecycleResume { block() }

/**
 * @see Lifecycle.launchOnLifecyclePause
 */
inline fun LifecycleOwner.launchOnLifecyclePause(
    crossinline block: suspend CoroutineScope.() -> Unit,
): Job = lifecycle.launchOnLifecyclePause { block() }

/**
 * @see Lifecycle.launchOnLifecycleStop
 */
inline fun LifecycleOwner.launchOnLifecycleStop(
    crossinline block: suspend CoroutineScope.() -> Unit,
): Job = lifecycle.launchOnLifecycleStop { block() }

/**
 * @see Lifecycle.launchOnLifecycleDestroy
 */
inline fun LifecycleOwner.launchOnLifecycleDestroy(
    crossinline block: suspend CoroutineScope.() -> Unit,
): Job = lifecycle.launchOnLifecycleDestroy { block() }

/**
 * @see Lifecycle.launchOnLifecycle
 */
inline fun LifecycleOwner.launchOnLifecycle(
    crossinline block: suspend CoroutineScope.(Lifecycle.Event) -> Unit,
): Job = lifecycle.launchOnLifecycle { block(it) }

/**
 * @see Lifecycle.launchOnLifecycleEvent
 */
inline fun LifecycleOwner.launchOnLifecycleEvent(
    event: Lifecycle.Event,
    crossinline block: suspend CoroutineScope.(Lifecycle.Event) -> Unit,
): Job = lifecycle.launchOnLifecycleEvent(event, block)
