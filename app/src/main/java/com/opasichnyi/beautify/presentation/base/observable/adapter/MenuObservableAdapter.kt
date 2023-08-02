package com.opasichnyi.beautify.presentation.base.observable.adapter

import androidx.annotation.AnyThread
import androidx.annotation.MenuRes
import com.opasichnyi.beautify.view.impl.entity.RootDataUIEntity

/**
 * Composition part [RootDataObservableAdapter]
 */
interface MenuObservableAdapter : CompositionRootDataObservableAdapter {

    @AnyThread fun getMenu(): RootDataUIEntity.Menu? = get(RootDataUIEntity.Menu::class)

    @AnyThread fun emitMenu(menu: RootDataUIEntity.Menu): Boolean = emit(menu)

    @AnyThread fun emitMenu(@MenuRes id: Int): Boolean = emitMenu(RootDataUIEntity.Menu(id))
}
