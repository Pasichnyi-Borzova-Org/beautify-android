package com.opasichnyi.beautify.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.annotation.CheckResult
import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.lenovo.smartoffice.common.util.extension.lifecycle.launchOnLifecycleAnyEvent
import com.lenovo.smartoffice.common.util.extension.lifecycle.launchOnLifecycleDestroy
import com.lenovo.smartoffice.common.util.extension.lifecycle.repeatOnStart
import com.opasichnyi.beautify.presentation.base.BaseViewModel
import com.opasichnyi.beautify.ui.ext.inflateBinding
import com.opasichnyi.beautify.util.ext.getGenericClass
import com.opasichnyi.beautify.util.ext.unsafeLazy
import org.koin.androidx.viewmodel.ext.android.getViewModel

open class BaseFragment<VB : ViewBinding, VM : BaseViewModel> :
    Fragment(), BaseView<VB, VM> {

    protected val viewModel: VM by unsafeLazy {
        getViewModel(clazz = javaClass.getGenericClass<VM>(genericPosition = 1).kotlin)
    }

    protected var binding: VB? = null
        private set

    @get:StyleRes
    protected open val themeRes: Int =
        android.R.style.Theme_Material_Light_DarkActionBar

    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater =
        super.onGetLayoutInflater(savedInstanceState)
            .cloneInContext(ContextThemeWrapper(requireContext(), themeRes))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflateBinding(inflater, container, false)
        .also(::binding::set)
        .also { viewLifecycleOwner.launchOnLifecycleDestroy { binding = null } }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initBackPressListener()
        requireBinding().let { binding ->
            onViewBound(
                binding = binding,
                savedInstanceState = savedInstanceState,
            )
            listenViewModel(viewModel, binding)
        }
    }

    private fun initBackPressListener() {
        val onBackPressedCallback = OnBackPressedCallbackWrapper(true, ::onBackPressed)

        viewLifecycleOwner.launchOnLifecycleAnyEvent { event ->
            onBackPressedCallback.isEnabled = event == Lifecycle.Event.ON_RESUME
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    protected open fun onBackPressed() {
        findNavController().apply {
            if (previousBackStackEntry != null) navigateUp() else requireActivity().finish()
        }
    }

    @CheckResult
    fun requireBackStackEntry(): NavBackStackEntry =
        findNavController().currentBackStackEntry
            ?: throw IllegalStateException(
                "Current NavBackStackEntry in fragment $this is null"
            )

    @CallSuper
    override fun listenViewModel(viewModel: VM, binding: VB) {
        viewLifecycleOwner.repeatOnStart {
            viewModel.progressStateFlow.collect {
                if (it) {
                    showProgress()
                } else {
                    hideProgress()
                }
            }
        }

        viewLifecycleOwner.repeatOnStart {

            viewModel.errorStateFlow.collect {
                if (it != null) {
                    showError(it)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    open fun showError(errorMessage: String) {
        getBaseActivity().showError(errorMessage)
    }

    open fun showProgress() {
        getBaseActivity().showProgress()
    }

    open fun hideProgress() {
        getBaseActivity().hideProgress()
    }

    @CheckResult
    protected fun requireBinding(): VB =
        binding ?: throw IllegalStateException("Binding in fragment $this is null")

    private fun getBaseActivity() = (requireActivity() as BaseActivity<*, *>)
}


class OnBackPressedCallbackWrapper(
    enabled: Boolean,
    private val handleOnBackPressed: () -> Unit,
) : OnBackPressedCallback(enabled) {

    override fun handleOnBackPressed() {
        handleOnBackPressed.invoke()
    }
}