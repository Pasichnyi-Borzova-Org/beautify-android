package com.opasichnyi.beautify.util.extension.widget

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.CheckResult
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.opasichnyi.beautify.util.extension.content.layoutInflater

/**
 * Get and cast to [ViewGroup.MarginLayoutParams]
 *
 * @see [ViewGroup.getLayoutParams]
 *
 * @return [ViewGroup.MarginLayoutParams] or throw [ClassCastException]
 */
@get:CheckResult inline val View.marginLayoutParams
    get() = layoutParams as ViewGroup.MarginLayoutParams?

/**
 * @see [android.content.Context.layoutInflater]
 */
@get:CheckResult inline val View.layoutInflater: LayoutInflater
    get() = context.layoutInflater

/**
 * @see [androidx.core.view.WindowCompat.getInsetsController]
 */
@CheckResult fun View.getInsetsController(activity: Activity): WindowInsetsControllerCompat =
    getInsetsController(activity.window)

/**
 * @see [androidx.core.view.WindowCompat.getInsetsController]
 */
@CheckResult fun View.getInsetsController(fragment: Fragment): WindowInsetsControllerCompat? =
    fragment.activity?.let { getInsetsController(it) }

/**
 * @see [androidx.core.view.WindowCompat.getInsetsController]
 */
@CheckResult fun View.getInsetsController(window: Window): WindowInsetsControllerCompat =
    WindowCompat.getInsetsController(window, this)
