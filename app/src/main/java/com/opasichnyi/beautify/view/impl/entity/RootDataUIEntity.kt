package com.opasichnyi.beautify.view.impl.entity

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.MenuRes
import com.opasichnyi.beautify.util.extension.resources.navigationIcon

/**
 * Base class for UI data,that delivered from child fragment to root UI view to display.
 * them in the toolbar.
 */
sealed class RootDataUIEntity {

    /**
     * Subclass of [RootDataUIEntity] that represents a title data.
     */
    data class Title(val title: CharSequence) : RootDataUIEntity()

    /**
     * Subclass of [RootDataUIEntity] that represents a menu data.
     */
    data class Menu(
        @MenuRes val menuId: Int,
        val isClearBeforeInflate: Boolean = true,
    ) : RootDataUIEntity()

    /**
     * Subclass of [RootDataUIEntity] that represents a navigation icon data.
     * You can change navigation icon resource in the activity theme.
     */
    data class NavIcon(val isVisible: Boolean) : RootDataUIEntity() {

        fun getNavigationIcon(context: Context): Drawable? =
            if (isVisible) context.navigationIcon else null
    }
}
