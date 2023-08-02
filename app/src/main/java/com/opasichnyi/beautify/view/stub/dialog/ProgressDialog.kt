package com.opasichnyi.beautify.view.stub.dialog

import androidx.appcompat.app.AppCompatDialog
import com.opasichnyi.beautify.databinding.DialogProgressBinding
import com.opasichnyi.beautify.presentation.stub.viewmodel.ProgressViewModel
import com.opasichnyi.beautify.view.base.dialog.BaseTransparentDialog

class ProgressDialog : BaseTransparentDialog<DialogProgressBinding, ProgressViewModel>() {

    override val isCanceledOnTouchOutside = false

    override fun onBackPressed(
        dialog: AppCompatDialog,
        onCancelListener: (AppCompatDialog) -> Unit,
    ) {
        // Blocking a call to a super function cancels back pressed
    }

    override fun listenViewModel(viewModel: ProgressViewModel) {
        super.listenViewModel(viewModel)
        viewModel.progressObservable.launchCollect(this) { visibility ->
            if (visibility) {
                // @see [BaseActivity.listenViewModel]
            } else {
                dismiss()
            }
        }
    }
}
