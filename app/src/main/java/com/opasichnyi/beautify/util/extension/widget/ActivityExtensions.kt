package com.opasichnyi.beautify.util.extension.widget

import android.app.Activity
import androidx.annotation.MainThread
import androidx.core.view.WindowInsetsControllerCompat

/**
 * Same what windowLightNavigationBar for [Activity] theme, but from 26 API+
 * You must use the windowLightNavigationBar for 27 API+ in theme.
 */
@get:MainThread @set:MainThread
inline var Activity.isLightNavigationBar: Boolean
    get() = WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars
    set(value) {
        WindowInsetsControllerCompat(window, window.decorView).run {
            isAppearanceLightNavigationBars = value
        }
    }
