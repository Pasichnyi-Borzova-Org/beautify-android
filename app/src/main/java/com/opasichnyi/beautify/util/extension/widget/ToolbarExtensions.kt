package com.opasichnyi.beautify.util.extension.widget

import androidx.annotation.MenuRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat

/**
 * @see android.view.Menu.clear
 * @see inflateMenu
 */
fun Toolbar.inflateMenu(@MenuRes menuRes: Int, clearBeforeInflate: Boolean = true) {
    if (menuRes == ResourcesCompat.ID_NULL) {
        menu?.clear()
    } else {
        if (clearBeforeInflate) {
            menu?.clear()
        }
        inflateMenu(menuRes)
    }
}
