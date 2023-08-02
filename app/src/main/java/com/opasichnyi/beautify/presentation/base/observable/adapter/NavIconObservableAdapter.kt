package com.opasichnyi.beautify.presentation.base.observable.adapter

import androidx.annotation.AnyThread
import com.opasichnyi.beautify.presentation.impl.entity.UIEvent
import com.opasichnyi.beautify.view.impl.entity.RootDataUIEntity
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Composition part [RootDataObservableAdapter]
 */
interface NavIconObservableAdapter : CompositionRootDataObservableAdapter {

    /**
     * [MutableStateFlow] that asks the UI to call the [emitNavIcon] with new data.
     */
    val navigationIconEnableEvent: MutableStateFlow<UIEvent<Unit>>

    @AnyThread fun getNavIcon(): RootDataUIEntity.NavIcon? = get(RootDataUIEntity.NavIcon::class)

    @AnyThread fun emitNavIcon(navIcon: RootDataUIEntity.NavIcon): Boolean = emit(navIcon)

    @AnyThread fun emitNavIcon(enabled: Boolean): Boolean =
        emitNavIcon(RootDataUIEntity.NavIcon(enabled))
}
