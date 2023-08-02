package com.opasichnyi.beautify.util.extension.flow

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

private val MIN_ACTIVE_STATE_DEFAULT = Lifecycle.State.STARTED

fun <T> Flow<T>.flowWithLifecycleOwner(
    lifecycleOwner: LifecycleOwner,
    minActiveState: Lifecycle.State = MIN_ACTIVE_STATE_DEFAULT,
): Flow<T> = flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState)

suspend fun <T> Flow<T>.collectWithLifecycleOwner(
    lifecycleOwner: LifecycleOwner,
    minActiveState: Lifecycle.State = MIN_ACTIVE_STATE_DEFAULT,
    collector: FlowCollector<T>,
) {
    flowWithLifecycleOwner(
        lifecycleOwner = lifecycleOwner,
        minActiveState = minActiveState,
    ).collect(collector)
}

suspend fun <T> Flow<T>.collectWithLifecycle(
    lifecycle: Lifecycle,
    minActiveState: Lifecycle.State = MIN_ACTIVE_STATE_DEFAULT,
    collector: FlowCollector<T>,
) {
    flowWithLifecycle(
        lifecycle = lifecycle,
        minActiveState = minActiveState,
    ).collect(collector)
}

fun <T> Flow<T>.launchCollectWithLifecycleOwner(
    lifecycleOwner: LifecycleOwner,
    minActiveState: Lifecycle.State = MIN_ACTIVE_STATE_DEFAULT,
    collector: FlowCollector<T>,
) {
    launchCollectWithLifecycle(
        lifecycle = lifecycleOwner.lifecycle,
        minActiveState = minActiveState,
        collector = collector,
    )
}

fun <T> Flow<T>.launchCollectWithLifecycle(
    lifecycle: Lifecycle,
    minActiveState: Lifecycle.State = MIN_ACTIVE_STATE_DEFAULT,
    collector: FlowCollector<T>,
) {
    lifecycle.coroutineScope.launch {
        collectWithLifecycle(
            lifecycle = lifecycle,
            minActiveState = minActiveState,
            collector = collector,
        )
    }
}
