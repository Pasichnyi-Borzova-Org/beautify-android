package com.lenovo.smartoffice.common.util.extension.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * @see Lifecycle.launchOnLifecycleCreate
 */
inline fun LifecycleOwner.launchOnLifecycleCreate(
    crossinline block: CoroutineScope.() -> Unit,
): Job = lifecycle.launchOnLifecycleCreate { block() }

/**
 * @see Lifecycle.launchOnLifecycleStart
 */
inline fun LifecycleOwner.launchOnLifecycleStart(
    crossinline block: CoroutineScope.() -> Unit,
): Job = lifecycle.launchOnLifecycleStart { block() }

/**
 * @see Lifecycle.launchOnLifecycleResume
 */
inline fun LifecycleOwner.launchOnLifecycleResume(
    crossinline block: CoroutineScope.() -> Unit,
): Job = lifecycle.launchOnLifecycleResume { block() }

/**
 * @see Lifecycle.launchOnLifecyclePause
 */
inline fun LifecycleOwner.launchOnLifecyclePause(
    crossinline block: CoroutineScope.() -> Unit,
): Job = lifecycle.launchOnLifecyclePause { block() }

/**
 * @see Lifecycle.launchOnLifecycleStop
 */
inline fun LifecycleOwner.launchOnLifecycleStop(
    crossinline block: CoroutineScope.() -> Unit,
): Job = lifecycle.launchOnLifecycleStop { block() }

/**
 * @see Lifecycle.launchOnLifecycleDestroy
 */
inline fun LifecycleOwner.launchOnLifecycleDestroy(
    crossinline block: CoroutineScope.() -> Unit,
): Job = lifecycle.launchOnLifecycleDestroy { block() }

/**
 * @see Lifecycle.launchOnLifecycleAnyEvent
 */
inline fun LifecycleOwner.launchOnLifecycleAnyEvent(
    crossinline block: CoroutineScope.(Lifecycle.Event) -> Unit,
): Job = lifecycle.launchOnLifecycleAnyEvent { block(it) }

/**
 * @see Lifecycle.launchOnLifecycleEvent
 */
inline fun LifecycleOwner.launchOnLifecycleEvent(
    event: Lifecycle.Event,
    crossinline block: CoroutineScope.(Lifecycle.Event) -> Unit,
): Job = lifecycle.launchOnLifecycleEvent(event, block)

inline fun LifecycleOwner.repeatOnStart(crossinline block: suspend () -> Unit) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            block()
        }
    }
}
