package com.opasichnyi.beautify.util.view.widget.recycler

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.material.R
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.opasichnyi.beautify.util.extension.reflect.getMutablePropertyNonNull
import com.opasichnyi.beautify.util.extension.resources.getDrawableCompat

class DefaultMaterialItemDecoration @JvmOverloads constructor(
    context: Context,
    @LinearLayoutCompat.OrientationMode orientation: Int = LinearLayoutCompat.VERTICAL,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = R.attr.materialDividerStyle,
) : MaterialDividerItemDecoration(context, attrs, defStyleAttr, orientation) {

    /**
     * Reflection for [MaterialDividerItemDecoration.dividerDrawable]
     */
    // TODO remove when will be allowed to modify divider drawable
    private val dividerDrawableField by lazy(LazyThreadSafetyMode.NONE) {
        MaterialDividerItemDecoration::class.getMutablePropertyNonNull<Drawable>(true)
    }

    // TODO remove when will be allowed to modify divider drawable
    var dividerDrawable: Drawable
        get() = dividerDrawableField.get(this)
        set(value) {
            dividerDrawableField.set(this, value)
            // Set tint to new divider drawable
            dividerColor = dividerColor
        }

    fun setDividerDrawableResource(context: Context, @DrawableRes drawableRes: Int) {
        dividerDrawable = context.getDrawableCompat(drawableRes)
            ?: throw Resources.NotFoundException()
    }
}
