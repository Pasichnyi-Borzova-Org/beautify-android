package com.opasichnyi.beautify.util.extension.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CheckResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import com.opasichnyi.beautify.presentation.base.viewmodel.BaseViewModel
import com.opasichnyi.beautify.util.extension.reflect.getFunFromGenericClass
import com.opasichnyi.beautify.util.extension.reflect.getGenericClass
import com.opasichnyi.beautify.view.base.BaseView
import com.opasichnyi.beautify.view.base.BaseView.Companion.GENERIC_VIEW_BINDING_POSITION
import com.opasichnyi.beautify.view.base.BaseView.Companion.GENERIC_VIEW_MODEL_POSITION
import kotlin.reflect.KFunction

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
fun <VB : ViewBinding> BaseView<VB, *>.inflateBinding(
    layoutInflater: LayoutInflater,
    position: Int = GENERIC_VIEW_BINDING_POSITION,
): VB =
    this::class
        .getFunFromGenericClass<VB>(position, true, ::isInflateWithoutParentFun)
        .call(layoutInflater)

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
    position: Int = GENERIC_VIEW_BINDING_POSITION,
): VB = this::class
    .getFunFromGenericClass<VB>(
        genericPosition = position,
        staticFun = true,
        conditionForSearchFun = ::isInflateWithParentFun,
    )
    .call(layoutInflater, parent, attachToParent)

/**
 * Check is method has only one input parameter with type [LayoutInflater] and static modifier.
 *
 * @see LayoutInflater
 * @see getFunFromGenericClass
 * @see isInflateWithoutParentFun
 *
 * @param function method that need to be checked.
 *
 * @return true if method was found, otherwise false.
 */
@CheckResult private fun isInflateWithoutParentFun(function: KFunction<*>): Boolean = function.run {
    parameters.size == 1 &&
        parameters[0].type.classifier == LayoutInflater::class
}

/**
 * Check is method has input parameters with types like [LayoutInflater], [ViewGroup], [Boolean]
 * with corresponding order and static modifier.
 *
 * @see LayoutInflater
 * @see getFunFromGenericClass
 * @see isInflateWithoutParentFun
 *
 * @param function method that need to be checked.
 *
 * @return true if method was found, otherwise false.
 */
@CheckResult private fun isInflateWithParentFun(function: KFunction<*>): Boolean = function.run {
    parameters.size == 3 &&
        parameters[0].type.classifier == LayoutInflater::class &&
        parameters[1].type.classifier == ViewGroup::class &&
        parameters[2].type.classifier == Boolean::class
}

/**
 * Create or get existing instance of generic type of ViewModel for current base view.
 *
 * @see getGenericClass
 * @see ViewModelProvider
 *
 * @return instance of generic type [VM] that implements [BaseViewModel].
 */
@CheckResult fun <VM, SO> SO.createViewModel(position: Int = GENERIC_VIEW_MODEL_POSITION): VM
    where VM : BaseViewModel,
          SO : BaseView<*, VM>,
          SO : ViewModelStoreOwner =
    ViewModelProvider(this)[this::class.getGenericClass<VM>(position).java]
