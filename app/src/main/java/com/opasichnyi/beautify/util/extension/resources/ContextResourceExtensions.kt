package com.opasichnyi.beautify.util.extension.resources

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat

/**
 * @see ContextCompat.getColor
 */
@ColorInt
@CheckResult
fun Context.getColorCompat(@ColorRes resId: Int): Int = ContextCompat.getColor(this, resId)

/**
 * @see AppCompatResources.getColorStateList
 */
@CheckResult fun Context.getColorStateListCompat(@ColorRes resId: Int): ColorStateList? =
    AppCompatResources.getColorStateList(this, resId)

/**
 * @see ContextCompat.getColor
 * @see ColorStateList.valueOf
 */
@CheckResult fun Context.getColorStateListFromColor(@ColorRes resId: Int): ColorStateList? =
    ColorStateList.valueOf(ContextCompat.getColor(this, resId))

/**
 * @see AppCompatResources.getDrawable
 */
@CheckResult fun Context.getDrawableCompat(@DrawableRes resId: Int): Drawable? =
    AppCompatResources.getDrawable(this, resId)

/**
 * Request system navigation icon drawable
 */
@get:CheckResult inline val Context.navigationIcon: Drawable?
    get() =
        getDrawableCompat(
            resId = TypedValue().also {
                theme?.resolveAttribute(androidx.appcompat.R.attr.navigationIcon, it, true)
            }.resourceId,
        )

/**
 * Request system id of action bar height for this orientation
 * and get dimension in a pixel size
 */
@get:Px @get:CheckResult
inline val Context.actionBarSize: Int?
    get() =
        TypedValue().let { typedValue ->
            if (theme.resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
                TypedValue.complexToDimensionPixelSize(typedValue.data, resources.displayMetrics)
            } else {
                null
            }
        }

/**
 * Request system id of color primary and return this color
 */
@get:ColorInt @get:CheckResult
inline val Context.colorPrimary: Int
    get() {
        var typedArray: TypedArray? = null

        try {
            val colorAttrRes = intArrayOf(android.R.attr.colorPrimary)
            typedArray = obtainStyledAttributes(TypedValue().data, colorAttrRes)
            return typedArray.getColor(0, 0)
        } finally {
            typedArray?.recycle()
        }
    }
