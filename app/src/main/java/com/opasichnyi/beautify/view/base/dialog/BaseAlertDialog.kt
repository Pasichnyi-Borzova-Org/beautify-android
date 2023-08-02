package com.opasichnyi.beautify.view.base.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialog
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.opasichnyi.beautify.R
import com.opasichnyi.beautify.presentation.base.viewmodel.BaseViewModel

/**
 * If you do not want to use a custom view, then use generics <[VB], [VM]> in
 * the implementation Name<[ViewBinding], [com.opasichnyi.beautify.presentation.stub.viewmodel.EmptyViewModel]>
 *
 * If you want to use the view from the binding, override the property [isCustomView]
 */
abstract class BaseAlertDialog<VB : ViewBinding, VM : BaseViewModel> : BaseDialog<VB, VM>() {

    protected open val isCustomView = false

    @StringRes protected open val messageRes: Int? = null
    protected open val message: CharSequence? get() = messageRes?.let(::getText)

    @StringRes protected open val positiveBtnRes: Int? = null
    protected open val positiveBtn: CharSequence? get() = positiveBtnRes?.let(::getText)
    protected open val positiveClickListener: DialogInterface.OnClickListener? = null

    @StringRes protected open val negativeBtnRes: Int? = null
    protected open val negativeBtn: CharSequence? get() = negativeBtnRes?.let(::getText)
    protected open val negativeClickListener: DialogInterface.OnClickListener? = null

    @StringRes protected open val neutralBtnRes: Int? = null
    protected open val neutralBtn: CharSequence? get() = neutralBtnRes?.let(::getText)
    protected open val neutralClickListener: DialogInterface.OnClickListener? = null

    override fun getTheme() = R.style.Theme_Application_Dialog_Alert

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(requireContext(), theme)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveBtn, positiveClickListener)
            .setNegativeButton(negativeBtn, negativeClickListener)
            .setNeutralButton(neutralBtn, neutralClickListener)
            .setCancelable(isCanceledOnTouchOutside)
            .create()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = if (isCustomView) super.onCreateView(inflater, container, savedInstanceState) else null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as AlertDialog).setView(view)
    }

    @Deprecated(
        message = "This fun will never be called.",
        replaceWith = ReplaceWith("onCancel(dialog)", "android.content.DialogInterface"),
        level = DeprecationLevel.ERROR,
    )
    override fun onBackPressed(
        dialog: AppCompatDialog,
        onCancelListener: (AppCompatDialog) -> Unit,
    ) {
        super.onBackPressed(dialog, onCancelListener)
    }

    /**
     * This method does not allow you to cancel the [onBackPressed] call.
     * Use [BaseDialog] for this task.
     */
    @Suppress("RedundantOverride")
    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }
}
