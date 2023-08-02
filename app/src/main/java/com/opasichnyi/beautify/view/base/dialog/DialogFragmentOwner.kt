package com.opasichnyi.beautify.view.base.dialog

import androidx.activity.ComponentDialog
import androidx.fragment.app.DialogFragment
import kotlin.reflect.jvm.jvmName

interface DialogFragmentOwner<D : ComponentDialog> {

    val dialogFragment get() = this as DialogFragment

    val dialogTag get() = dialogFragment::class.jvmName

    /**
     * Blocking a call to a this function cancels back pressed
     */
    fun onBackPressed(dialog: D, onCancelListener: (D) -> Unit) {
        onCancelListener(dialog)
    }

    /**
     * Dismiss without Exception
     */
    fun dismissSilent() {
        dialogFragment.dismissAllowingStateLoss()
    }
}
