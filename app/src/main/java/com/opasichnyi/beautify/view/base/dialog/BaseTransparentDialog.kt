package com.opasichnyi.beautify.view.base.dialog

import androidx.viewbinding.ViewBinding
import com.opasichnyi.beautify.R
import com.opasichnyi.beautify.presentation.base.viewmodel.BaseViewModel

/**
 * Base transparent dialog fragment
 */
abstract class BaseTransparentDialog<VB : ViewBinding, VM : BaseViewModel> : BaseDialog<VB, VM>() {

    override val style: Int get() = STYLE_NO_FRAME

    override fun getTheme() = R.style.Theme_Application_Dialog_Transparent
}
