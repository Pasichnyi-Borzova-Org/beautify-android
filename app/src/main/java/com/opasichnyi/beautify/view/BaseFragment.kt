package com.opasichnyi.beautify.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcher
import androidx.annotation.CallSuper
import androidx.annotation.CheckResult
import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.Navigation.findNavController
import androidx.viewbinding.ViewBinding
import com.lenovo.smartoffice.common.util.extension.lifecycle.launchOnLifecycleDestroy
import com.opasichnyi.beautify.presentation.base.BaseViewModel
import com.opasichnyi.beautify.util.ext.getGenericClass
import com.opasichnyi.beautify.util.ext.unsafeLazy
import com.opasichnyi.beautify.view.ext.inflateBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class BaseFragment<VB : ViewBinding, VM : BaseViewModel> :
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
        requireBinding().let { binding ->
            onViewBound(
                binding = binding,
                savedInstanceState = savedInstanceState,
            )
            listenViewModel(viewModel, binding)
        }
    }

    @CallSuper
    override fun listenViewModel(viewModel: VM, binding: VB) {
        viewModel.unexpectedErrorLiveEvent.observe(viewLifecycleOwner) {
            handleError(binding, it)
        }
    }

    @CheckResult
    protected fun requireBinding(): VB =
        binding ?: throw IllegalStateException("Binding in fragment $this is null")
}