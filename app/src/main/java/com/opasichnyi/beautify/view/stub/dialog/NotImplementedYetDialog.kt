package com.opasichnyi.beautify.view.stub.dialog

import com.opasichnyi.beautify.R
import com.opasichnyi.beautify.databinding.FragmentNotImplementedYetBinding
import com.opasichnyi.beautify.presentation.stub.viewmodel.EmptyViewModel
import com.opasichnyi.beautify.view.base.dialog.BaseAlertDialog

class NotImplementedYetDialog :
    BaseAlertDialog<FragmentNotImplementedYetBinding, EmptyViewModel>() {

    override val titleRes = R.string.not_implemented_yet

    override val positiveBtnRes = R.string.ok

    override val neutralBtnRes = R.string.cancel
}
