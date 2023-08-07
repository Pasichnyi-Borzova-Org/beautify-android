package com.opasichnyi.beautify.view

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.opasichnyi.beautify.presentation.base.BaseViewModel
import com.opasichnyi.beautify.util.ext.getGenericClass
import com.opasichnyi.beautify.util.ext.unsafeLazy
import com.opasichnyi.beautify.view.ext.inflateBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel

class BaseActivity<VB : ViewBinding, VM : BaseViewModel> : AppCompatActivity(),
    BaseView<VB, VM> {


    protected val viewModel: VM by unsafeLazy {
        getViewModel(clazz = javaClass.getGenericClass<VM>(genericPosition = 1).kotlin)
    }

    protected val binding: VB by unsafeLazy { inflateBinding(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.also {
            setContentView(it.root)

            onViewBound(it, savedInstanceState)
            listenViewModel(viewModel, binding)
        }
    }

    @CallSuper
    override fun listenViewModel(viewModel: VM, binding: VB) {
        viewModel.unexpectedErrorLiveEvent.observe(this@BaseActivity) {
            handleError(binding, it)
        }
    }
}
