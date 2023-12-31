package com.lenovo.smartoffice.common.util.extension.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.coroutineScope
import com.opasichnyi.beautify.util.ext.awaitCancellation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Function sets and executes callback function [Lifecycle.Event.ON_CREATE] lifecycle event.
 * Cancel callback on lifecycle coroutine scope cancellation.
 *
 * @see Lifecycle.Event
 * @see Lifecycle.launchOnLifecycleEvent
 *
 * @param block callback object that executes some action.
 *
 * @return implementation of [Job].
 */
inline fun Lifecycle.launchOnLifecycleCreate(
    crossinline block: CoroutineScope.() -> Unit,
): Job = launchOnLifecycleEvent(Lifecycle.Event.ON_CREATE) { block() }

/**
 * Function sets and executes callback function [Lifecycle.Event.ON_START] lifecycle event.
 * Cancel callback on lifecycle coroutine scope cancellation.
 *
 * @see Lifecycle.Event
 * @see Lifecycle.launchOnLifecycleEvent
 *
 * @param block callback object that executes some action.
 *
 * @return implementation of [Job].
 */
inline fun Lifecycle.launchOnLifecycleStart(
    crossinline block: CoroutineScope.() -> Unit,
): Job = launchOnLifecycleEvent(Lifecycle.Event.ON_START) { block() }

/**
 * Function sets and executes callback function [Lifecycle.Event.ON_RESUME] lifecycle event.
 * Cancel callback on lifecycle coroutine scope cancellation.
 *
 * @see Lifecycle.Event
 * @see Lifecycle.launchOnLifecycleEvent
 *
 * @param block callback object that executes some action.
 *
 * @return implementation of [Job].
 */
inline fun Lifecycle.launchOnLifecycleResume(
    crossinline block: CoroutineScope.() -> Unit,
): Job = launchOnLifecycleEvent(Lifecycle.Event.ON_RESUME) { block() }

/**
 * Function sets and executes callback function [Lifecycle.Event.ON_PAUSE] lifecycle event.
 * Cancel callback on lifecycle coroutine scope cancellation.
 *
 * @see Lifecycle.Event
 * @see Lifecycle.launchOnLifecycleEvent
 *
 * @param block callback object that executes some action.
 *
 * @return implementation of [Job].
 */
inline fun Lifecycle.launchOnLifecyclePause(
    crossinline block: CoroutineScope.() -> Unit,
): Job = launchOnLifecycleEvent(Lifecycle.Event.ON_PAUSE) { block() }

/**
 * Function sets and executes callback function [Lifecycle.Event.ON_STOP] lifecycle event.
 * Cancel callback on lifecycle coroutine scope cancellation.
 *
 * @see Lifecycle.Event
 * @see Lifecycle.launchOnLifecycleEvent
 *
 * @param block callback object that executes some action.
 *
 * @return implementation of [Job].
 */
inline fun Lifecycle.launchOnLifecycleStop(
    crossinline block: CoroutineScope.() -> Unit,
): Job = launchOnLifecycleEvent(Lifecycle.Event.ON_STOP) { block() }

/**
 * Function sets and executes callback function for [Lifecycle.Event.ON_DESTROY] lifecycle event.
 * Cancel callback on lifecycle coroutine scope cancellation.
 *
 * @see Lifecycle.Event
 * @see Lifecycle.launchOnLifecycleEvent
 *
 * @param block callback object that executes some action.
 *
 * @return implementation of [Job].
 */
inline fun Lifecycle.launchOnLifecycleDestroy(
    crossinline block: CoroutineScope.() -> Unit,
): Job = launchOnLifecycleEvent(Lifecycle.Event.ON_DESTROY) { block() }

/**
 * Function sets and executes callback function for [Lifecycle.Event.ON_ANY] lifecycle event.
 * Cancel callback on lifecycle coroutine scope cancellation.
 *
 * @see Lifecycle.Event
 * @see Lifecycle.launchOnLifecycleEvent
 *
 * @param block callback object that executes some action.
 *
 * @return implementation of [Job].
 */
inline fun Lifecycle.launchOnLifecycleAnyEvent(
    crossinline block: CoroutineScope.(Lifecycle.Event) -> Unit,
): Job = launchOnLifecycleEvent(Lifecycle.Event.ON_ANY, block)

/**
 * Function sets and executes callback function for particular lifecycle event.
 * Cancel callback on lifecycle coroutine scope cancellation.
 *
 * @see Lifecycle.Event
 * @see LifecycleEventObserver
 * @see awaitCancellation
 *
 * @param event lifecycle event we execute callback on.
 * @param block callback object that executes some action.
 *
 * @return implementation of [Job].
 */
inline fun Lifecycle.launchOnLifecycleEvent(
    event: Lifecycle.Event,
    crossinline block: CoroutineScope.(Lifecycle.Event) -> Unit,
): Job = coroutineScope.launch {
    val lifecycleObserver = LifecycleEventObserver { _, newEvent ->
        if (event == Lifecycle.Event.ON_ANY || event == newEvent) {
            block(newEvent)
        }
    }

    this@launchOnLifecycleEvent.addObserver(lifecycleObserver)

    awaitCancellation {
        this@launchOnLifecycleEvent.removeObserver(lifecycleObserver)
    }
}
