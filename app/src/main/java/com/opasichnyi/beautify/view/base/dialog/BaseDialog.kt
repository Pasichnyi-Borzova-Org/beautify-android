package com.opasichnyi.beautify.view.base.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CheckResult
import androidx.annotation.IntDef
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.opasichnyi.beautify.R
import com.opasichnyi.beautify.presentation.base.viewmodel.BaseViewModel
import com.opasichnyi.beautify.util.extension.common.unsafeLazy
import com.opasichnyi.beautify.util.extension.lifecycle.launchOnLifecycleDestroy
import com.opasichnyi.beautify.util.extension.widget.createViewModel
import com.opasichnyi.beautify.util.extension.widget.inflateBinding
import com.opasichnyi.beautify.view.base.BaseView
import timber.log.Timber

abstract class BaseDialog<VB : ViewBinding, VM : BaseViewModel> :
    InjectableDialog(),
    BaseView<VB, VM>,
    DialogFragmentOwner<AppCompatDialog> {

    protected val viewModel: VM by unsafeLazy(::createViewModel)

    protected var binding: VB? = null
        private set

    protected open val isCanceledOnTouchOutside = true

    @StringRes protected open val titleRes: Int? = null

    protected open val title: CharSequence? by unsafeLazy {
        titleRes?.let(::getText)
    }

    @DialogStyle protected open val style: Int
        get() = if (title == null) STYLE_NO_TITLE else STYLE_NORMAL

    val navController: NavController by unsafeLazy { findNavController() }

    override fun getTheme() = R.style.Theme_Application_Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(style, theme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        object : AppCompatDialog(requireContext(), theme) {
            override fun cancel() {
                this@BaseDialog.onBackPressed(this) { super.cancel() }
            }
        }.apply {
            setTitle(title)
            setCanceledOnTouchOutside(isCanceledOnTouchOutside)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflateBinding(inflater, container, false)
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
     * Copy from [androidx.fragment.app.DialogFragment].DialogStyle
     */
    @IntDef(STYLE_NORMAL, STYLE_NO_TITLE, STYLE_NO_FRAME, STYLE_NO_INPUT)
    @Retention(AnnotationRetention.SOURCE)
    annotation class DialogStyle
}
