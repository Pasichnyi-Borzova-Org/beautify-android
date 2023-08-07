package com.opasichnyi.beautify.view.ext

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CheckResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import com.opasichnyi.beautify.presentation.base.BaseViewModel
import com.opasichnyi.beautify.util.ext.getFunFromGenericClass
import com.opasichnyi.beautify.util.ext.getGenericClass
import com.opasichnyi.beautify.view.BaseView
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/**
 * Create instance of ViewBinding for particular base view without parent, like an activity.
 *
 * @see LayoutInflater.inflate
 * @see getFunFromGenericClass
 * @see isInflateWithoutParentFun
 *
 * @return instance of generic type [VB] that implements [ViewBinding].
 */
@CheckResult
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> BaseView<VB, *>.inflateBinding(layoutInflater: LayoutInflater): VB =
    javaClass
        .getFunFromGenericClass<VB>(0, ::isInflateWithoutParentFun)
        .invoke(null, layoutInflater) as VB

/**
 * Create instance of ViewBinding for particular base view that has parent, like a fragment.
 *
 * @see LayoutInflater.inflate
 * @see getFunFromGenericClass
 * @see isInflateWithParentFun
 *
 * @return instance of generic type [VB] that implements [ViewBinding].
 */
@CheckResult
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> BaseView<VB, *>.inflateBinding(
    layoutInflater: LayoutInflater,
    parent: ViewGroup?,
    attachToParent: Boolean,
): VB = javaClass
    .getFunFromGenericClass<VB>(0, ::isInflateWithParentFun)
    .invoke(null, layoutInflater, parent, attachToParent) as VB

/**
 * Check is method has only one input parameter with type [LayoutInflater] and static modifier.
 *
 * @see LayoutInflater
 * @see getFunFromGenericClass
 * @see isInflateWithoutParentFun
 *
 * @param method method that need to be checked.
 *
 * @return true if method was found, otherwise false.
 */
@CheckResult private fun isInflateWithoutParentFun(method: Method): Boolean = method.run {
    Modifier.isStatic(modifiers) &&
            parameterTypes.size == 1 &&
            parameterTypes.first() == LayoutInflater::class.java
}

/**
 * Check is method has input parameters with types like [LayoutInflater], [ViewGroup], [Boolean]
 * with corresponding order and static modifier.
 *
 * @see LayoutInflater
 * @see getFunFromGenericClass
 * @see isInflateWithoutParentFun
 *
 * @param method method that need to be checked.
 *
 * @return true if method was found, otherwise false.
 */
@CheckResult private fun isInflateWithParentFun(method: Method): Boolean = method.run {
    Modifier.isStatic(modifiers) &&
            parameterTypes.size == 3 &&
            parameterTypes[0] == LayoutInflater::class.java &&
            parameterTypes[1] == ViewGroup::class.java &&
            parameterTypes[2] == Boolean::class.java
}

/**
 * Create or get existing instance of generic type of ViewModel for current base view.
 *
 * @see getGenericClass
 * @see ViewModelProvider
 *
 * @return instance of generic type [VM] that implements [BaseViewModel].
 */
@CheckResult fun <VM, SO> SO.createViewModel(): VM where
        VM : BaseViewModel,
        SO : BaseView<*, VM>,
        SO : ViewModelStoreOwner =
    ViewModelProvider(
        owner = this
    )[javaClass.getGenericClass(1)]
