package com.opasichnyi.beautify.presentation.base.observable

import androidx.annotation.AnyThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.opasichnyi.beautify.view.impl.entity.RootDataUIEntity

/**
 * Allows you to subscribe to changes in UI data intended for the root UI view.
 */
interface RootDataObservable {

    /**
     * Observe root data holder. @see [kotlinx.coroutines.flow.Flow.collect]
     */
    @AnyThread fun launchCollect(
        owner: LifecycleOwner,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        collector: (RootDataUIEntity) -> Unit,
    )
}
