package com.opasichnyi.beautify.util.extension.widget

import android.app.Activity
import android.view.View
import android.view.Window
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import timber.log.Timber

fun View.showKeyboard(window: Window) {
    post {
        requestFocus()
        getInsetsController(window).show(WindowInsetsCompat.Type.ime())
    }
}

fun View.hideKeyboard(window: Window) {
    post {
        getInsetsController(window).hide(WindowInsetsCompat.Type.ime())
    }
}

fun Activity.showKeyboard() {
    currentFocus?.showKeyboard(window) ?: Timber.w("Cannot show keyboard, current focus is null")
}

fun Activity.hideKeyboard() {
    currentFocus?.hideKeyboard(window) ?: Timber.w("Cannot hide keyboard, current focus is null")
}

fun Fragment.showKeyboard() {
    activity?.showKeyboard() ?: Timber.w("Cannot show keyboard, current activity is null")
}

fun Fragment.hideKeyboard() {
    activity?.hideKeyboard() ?: Timber.w("Cannot hide keyboard, current activity is null")
}
