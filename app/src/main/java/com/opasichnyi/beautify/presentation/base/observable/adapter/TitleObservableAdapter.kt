package com.opasichnyi.beautify.presentation.base.observable.adapter

import androidx.annotation.AnyThread
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import com.opasichnyi.beautify.view.impl.entity.RootDataUIEntity

/**
 * Composition part [RootDataObservableAdapter]
 */
interface TitleObservableAdapter : CompositionRootDataObservableAdapter {

    @AnyThread fun getTitle(): RootDataUIEntity.Title? = get(RootDataUIEntity.Title::class)

    @AnyThread fun emitTitle(title: RootDataUIEntity.Title): Boolean = emit(title)

    @AnyThread fun emitTitle(title: CharSequence): Boolean =
        emitTitle(RootDataUIEntity.Title(title))

    @AnyThread fun emitTitle(@StringRes id: Int): Boolean =
        emitTitle(if (id == ResourcesCompat.ID_NULL) "" else resources.getText(id))

    @AnyThread fun emitTitle(@StringRes id: Int, arg: CharSequence): Boolean =
        emitTitle(if (id == ResourcesCompat.ID_NULL) "" else resources.getText(id, arg))

    @AnyThread fun emitTitle(@StringRes id: Int, vararg args: Any): Boolean =
        emitTitle(if (id == ResourcesCompat.ID_NULL) "" else resources.getString(id, *args))
}
