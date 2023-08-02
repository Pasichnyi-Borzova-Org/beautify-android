package com.opasichnyi.beautify.view.base.activity

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.CheckResult
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import com.opasichnyi.beautify.R
import com.opasichnyi.beautify.presentation.base.viewmodel.BaseViewModel
import com.opasichnyi.beautify.util.extension.common.unsafeLazy
import com.opasichnyi.beautify.util.extension.widget.createViewModel
import com.opasichnyi.beautify.util.extension.widget.inflateBinding
import com.opasichnyi.beautify.view.base.BaseView
import com.opasichnyi.beautify.view.stub.dialog.ProgressDialog

abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel> :
    InjectableAppCompatActivity(),
    BaseView<VB, VM> {

    protected val viewModel: VM by unsafeLazy(::createViewModel)

    protected val binding: VB by unsafeLazy { inflateBinding(layoutInflater) }

    val navController: NavController by unsafeLazy {
        findNavController(R.id.fragmentContainerView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.also { binding ->
            setContentView(binding.root)
            onViewBound(binding, savedInstanceState)
            listenViewModel(viewModel)
        }
    }

    @CallSuper override fun listenViewModel(viewModel: VM) {
        super.listenViewModel(viewModel)
        viewModel.progressObservable.launchCollect(this) { visibility ->
            if (visibility) {
                ProgressDialog().show(supportFragmentManager)
            } else {
                // @see [ProgressDialog.listenViewModel]
            }
        }
    }

    override fun getViewLifecycleOwner() = this

    @CheckResult override fun requireBinding(): VB = binding
}
