package com.opasichnyi.beautify.presentation.base.observable.adapter

import androidx.annotation.AnyThread
import com.opasichnyi.beautify.presentation.base.observable.RootDataObservable
import com.opasichnyi.beautify.presentation.base.observable.WriteRootDataObservable
import com.opasichnyi.beautify.presentation.impl.observable.RootDataObservableImpl
import com.opasichnyi.beautify.view.impl.entity.RootDataUIEntity
import kotlin.reflect.KClass

/**
 * [Adapter](https://en.wikipedia.org/wiki/Adapter_pattern) that simplifies the work with
 * the [RootDataObservable]
 *
 * If you extend this interface with your new data,
 * you can add functions to each way of interacting with the data.
 *
 * (Example [emitTitle]: [postTitle(@StringRes id: Int)] and [postTitle(title: String)])
 *
 * Or change the interface: from [RootDataObservable] to [WriteRootDataObservable]
 * and use publishing directly [WriteRootDataObservable.emit] withe key and value.
 */
interface RootDataObservableAdapter :
    TitleObservableAdapter,
    MenuObservableAdapter,
    NavIconObservableAdapter {

    /**
     * Invalidate the current cached [RootDataUIEntity] in the root [RootDataObservableImpl]
     * and post the latest [RootDataUIEntity] relevant to the current screen data model into it
     * (as example [androidx.lifecycle.ViewModel]/activity/fragment).
     *
     * @param key one of the sealed [RootDataUIEntity] classes.
     */
    @AnyThread fun invalidateRootData(key: KClass<out RootDataUIEntity>? = null): Boolean
}
