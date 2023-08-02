package com.opasichnyi.beautify.util.extension.widget

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.opasichnyi.beautify.presentation.base.viewmodel.BaseViewModel
import com.opasichnyi.beautify.util.extension.reflect.getGenericClass
import com.opasichnyi.beautify.view.base.BaseView.Companion.GENERIC_VIEW_MODEL_POSITION
import com.opasichnyi.beautify.view.base.activity.BaseActivity
import com.opasichnyi.beautify.view.base.fragment.BaseFragment

@get:CheckResult inline val Fragment.baseActivity get() = activity as BaseActivity<*, *>?

@get:CheckResult val Fragment.parentBaseFragment: BaseFragment<*, *>?
    get() = parentFragment as? BaseFragment<*, *>?

/**
 * Tries to find the [BaseFragment] bypassing the [NavHostFragment].
 */
@CheckResult fun Fragment.findNearestParentBaseFragment(): BaseFragment<*, *>? =
    parentBaseFragment ?: parentFragment?.findNearestParentBaseFragment()

/**
 * @see findRootBaseFragment
 */
@get:CheckResult val Fragment.rootBaseFragment: BaseFragment<*, *>? get() = findRootBaseFragment()

/**
 * Find root fragment for child fragments.
 *
 * @return root fragment, if root fragment wasn't found - return null.
 */
@CheckResult private tailrec fun Fragment?.findRootBaseFragment(): BaseFragment<*, *>? =
    if (this?.findNearestParentBaseFragment() == null) {
        this as BaseFragment<*, *>?
    } else {
        parentFragment.findRootBaseFragment()
    }

@get:CheckResult inline val Fragment.viewLifecycleScope: LifecycleCoroutineScope
    get() = viewLifecycleOwner.lifecycleScope

@CheckResult fun Fragment.requireBaseActivity(): BaseActivity<*, *> =
    baseActivity ?: error("Fragment $this does not have parent base activity")

@CheckResult fun Fragment.requireParentBaseFragment(): BaseFragment<*, *> =
    parentBaseFragment ?: error("Fragment $this does not have parent base fragment")

@CheckResult fun Fragment.requireNearestParentBaseFragment(): BaseFragment<*, *> =
    findNearestParentBaseFragment() ?: error("Fragment $this does not have parent base fragment")

@CheckResult fun Fragment.requireRootBaseFragment(): BaseFragment<*, *> =
    rootBaseFragment ?: error("Fragment $this does not have root base fragment")

/**
 * Get instance of ViewModel of current activity for fragment.
 *
 * @see getGenericClass
 * @see createViewModelLazy
 *
 * @return instance of parameter [VM] of current activity for fragment,
 * if instance was not found - create a new one.
 */
@MainThread @CheckResult
fun <VM : BaseViewModel> Fragment.getActivityViewModel(
    factory: ViewModelProvider.Factory? = null,
    position: Int = GENERIC_VIEW_MODEL_POSITION,
): VM =
    requireBaseActivity().let {
        ViewModelProvider(
            owner = it,
            factory = factory ?: it.defaultViewModelProviderFactory,
        )[it::class.getGenericClass<VM>(position).java]
    }
