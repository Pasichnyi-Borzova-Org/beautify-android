package com.opasichnyi.beautify.ui.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.opasichnyi.beautify.presentation.base.BaseViewModel

interface BaseView<VB : ViewBinding, VM : BaseViewModel> {

    /**
     * Must be called after view binding was created. For example within activity
     * in onCreate method or within fragment in onViewCreated method
     *
     * @param binding instance of created view binding
     * @param savedInstanceState bundle that contains last UI saved state
     */
    fun onViewBound(binding: VB, savedInstanceState: Bundle?) {
        // Override if need and make other initialization
    }

    /**
     * Within this method ViewModel listening must be implemented.
     *
     * @param viewModel instance of [BaseViewModel]
     */
    fun listenViewModel(viewModel: VM, binding: VB) {
        // Override if need and make other initialization
    }

    /**
     * Must be used for handling of ViewModel exceptions.
     *
     * @param binding instance of created view binding.
     * @param throwable throwable that came from ViewModel.
     */
    fun handleError(binding: VB, throwable: Throwable) {
        TODO("Override and implement error handling logic")
    }
}