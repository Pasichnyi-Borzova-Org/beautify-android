package com.opasichnyi.beautify.presentation.base.observable

import androidx.annotation.AnyThread
import com.opasichnyi.beautify.view.impl.entity.RootDataUIEntity

/**
 * An interface with an observer for [RootDataUIEntity] that allows you to [emit] new data.
 */
interface WriteRootDataObservable : ReadRootDataObservable {

    /**
     * Tries to update root data.
     * If data do not exist - store it,
     *
     * WARNING!
     * Be careful when calling [emit] from background thread that not bound to view!
     * For instance, when you pass root data inside previous fragment viewmodel within
     * the same root fragment, it might overwrite the current fragment title for example.
     *
     * @param newData new data of [RootDataUIEntity].
     *
     * @return returns false if such data has already been posted and emitted.
     */
    @AnyThread fun emit(newData: RootDataUIEntity): Boolean
}
