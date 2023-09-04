package com.opasichnyi.beautify.ui.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.lenovo.smartoffice.common.util.extension.lifecycle.repeatOnStart
import com.opasichnyi.beautify.R
import com.opasichnyi.beautify.presentation.base.BaseViewModel
import com.opasichnyi.beautify.ui.ext.inflateBinding
import com.opasichnyi.beautify.util.ext.getGenericClass
import com.opasichnyi.beautify.util.ext.unsafeLazy
import org.koin.androidx.viewmodel.ext.android.getViewModel

open class BaseActivity<VB : ViewBinding, VM : BaseViewModel> : AppCompatActivity(),
    BaseView<VB, VM> {

    protected val viewModel: VM by unsafeLazy {
        getViewModel(clazz = javaClass.getGenericClass<VM>(genericPosition = 1).kotlin)
    }

    protected val binding: VB by unsafeLazy { inflateBinding(layoutInflater) }

    private lateinit var progressDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupProgressDialog()

        binding.also {
            setContentView(it.root)

            onViewBound(it, savedInstanceState)
            listenViewModel(viewModel, binding)
        }
    }

    private fun setupProgressDialog() {
        progressDialog = MaterialAlertDialogBuilder(this)
            .setView(R.layout.progress_dialog)
            .setCancelable(false)
            .setBackground(ColorDrawable(Color.TRANSPARENT))
            .create()
    }

    open fun showError(errorMessage: String) {

        Snackbar.make(
            findViewById<View>(android.R.id.content).rootView,
            errorMessage,
            Snackbar.LENGTH_LONG
        ).show()
    }

    open fun showProgress() {
        progressDialog.show()
    }

    open fun hideProgress() {
        progressDialog.cancel()
    }

    @CallSuper
    override fun listenViewModel(viewModel: VM, binding: VB) {
        repeatOnStart {
            viewModel.progressStateFlow.collect {
                if (it) {
                    showProgress()
                } else {
                    hideProgress()
                }
            }
        }

        repeatOnStart {

            viewModel.errorStateFlow.collect {
                if (it != null) {
                    showError(it)
                }
            }
        }
    }
}
