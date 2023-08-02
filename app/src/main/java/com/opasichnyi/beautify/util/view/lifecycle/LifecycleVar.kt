package com.opasichnyi.beautify.util.view.lifecycle

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import com.opasichnyi.beautify.util.extension.lifecycle.isDestroyed
import com.opasichnyi.beautify.util.extension.lifecycle.launchOnLifecycleDestroy
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Create instance of [LifecycleVar]
 *
 * @see LifecycleVar
 *
 * @return implementation of [ReadWriteProperty], in this case it's [LifecycleVar].
 */
@CheckResult
@MainThread
@Suppress("unused")
fun <T> Delegates.lifecycle(
    lifecycleOwnerProvider: () -> LifecycleOwner,
    initializer: () -> T,
): ReadWriteProperty<Any, T?> =
    LifecycleVar(lifecycleOwnerProvider, initializer)

/**
 * Class that initialize value lazily and set it to null on lifecycle destroy.
 *
 * @see LifecycleOwner
 * @see ReadWriteProperty
 * @see launchOnLifecycleDestroy
 *
 * @property value stores current object.
 * @property lifecycleOwnerProvider object that provides lifecycle owner object.
 * @property initializer object that make value initialization.
 */
private class LifecycleVar<T>(
    private val lifecycleOwnerProvider: () -> LifecycleOwner,
    private val initializer: () -> T,
) : ReadWriteProperty<Any, T?> {

    private var value: T? = null

    override fun getValue(thisRef: Any, property: KProperty<*>): T? =
        if (value == null) {
            val lifecycleOwner = lifecycleOwnerProvider()

            if (lifecycleOwner.lifecycle.isDestroyed) {
                null
            } else {
                initializer.invoke()?.also { initializedValue ->
                    value = initializedValue
                    lifecycleOwner.launchOnLifecycleDestroy { value = null }
                }
            }
        } else {
            value
        }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        this.value = value
    }
}
