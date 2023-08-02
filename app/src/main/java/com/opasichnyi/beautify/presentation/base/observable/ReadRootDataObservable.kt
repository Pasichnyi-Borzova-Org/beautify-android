package com.opasichnyi.beautify.presentation.base.observable

import androidx.annotation.AnyThread
import com.opasichnyi.beautify.view.impl.entity.RootDataUIEntity
import kotlin.reflect.KClass

/**
 * Observable which caches the [RootDataUIEntity] that was posted last.
 */
interface ReadRootDataObservable : RootDataObservable {

    /**
     * @param key one of the sealed [RootDataUIEntity] classes.
     * @return get cached data that was last posted.
     */
    @AnyThread fun <T : RootDataUIEntity> get(key: KClass<out T>): T?
}
