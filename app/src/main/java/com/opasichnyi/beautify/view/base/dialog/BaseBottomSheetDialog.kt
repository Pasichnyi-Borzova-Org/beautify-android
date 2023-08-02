package com.opasichnyi.beautify.view.base.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CheckResult
import androidx.annotation.IntDef
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.opasichnyi.beautify.R
import com.opasichnyi.beautify.presentation.base.viewmodel.BaseViewModel
import com.opasichnyi.beautify.util.extension.common.unsafeLazy
import com.opasichnyi.beautify.util.extension.lifecycle.launchOnLifecycleDestroy
import com.opasichnyi.beautify.util.extension.widget.createViewModel
import com.opasichnyi.beautify.util.extension.widget.inflateBinding
import com.opasichnyi.beautify.view.base.BaseView
import timber.log.Timber

abstract class BaseBottomSheetDialog<VB : ViewBinding, VM : BaseViewModel> :
    InjectableBottomSheetDialog(),
    BaseView<VB, VM>,
    DialogFragmentOwner<BottomSheetDialog> {

    protected val viewModel: VM by unsafeLazy(::createViewModel)

    protected var binding: VB? = null
        private set

    protected open val isCanceledOnTouchOutside = true

    @StableState protected open val state: Int = BottomSheetBehavior.STATE_EXPANDED

    val navController: NavController by unsafeLazy { findNavController() }

    override fun getTheme() = R.style.Theme_Application_BottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        object : BottomSheetDialog(requireContext(), theme) {
            override fun cancel() {
                this@BaseBottomSheetDialog.onBackPressed(this) { super.cancel() }
            }
        }.apply {
            behavior.state = state
            setCanceledOnTouchOutside(isCanceledOnTouchOutside)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflateBinding(inflater, container, false)
        .also(::binding::set)
        .also { viewLifecycleOwner.launchOnLifecycleDestroy { binding = null } }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.let { onViewBound(it, savedInstanceState) }
        listenViewModel(viewModel)
    }

    @CheckResult override fun requireBinding(): VB =
        binding ?: error("Binding in fragment $this is null")

    override fun dismiss() {
        runCatching { super.dismiss() }.onFailure {
            Timber.e(it)
            dismissSilent()
        }
    }

    override fun showNow(manager: FragmentManager, tag: String?) {
        super.showNow(manager, tag ?: dialogTag)
    }

    fun show(manager: FragmentManager) {
        show(manager, dialogTag)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag ?: dialogTag)
    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int =
        super.show(transaction, tag ?: dialogTag)

    /**
     * Copy from [BottomSheetBehavior.state]
     */
    @IntDef(
        BottomSheetBehavior.STATE_EXPANDED,
        BottomSheetBehavior.STATE_COLLAPSED,
        BottomSheetBehavior.STATE_HALF_EXPANDED,
        BottomSheetBehavior.STATE_HIDDEN,
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class StableState
}
