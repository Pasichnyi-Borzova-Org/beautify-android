package com.opasichnyi.beautify.util.extension.resources

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.core.content.res.ResourcesCompat
import com.opasichnyi.beautify.util.resource.Dp

/**
 * @see ResourcesCompat.getDrawable
 *
 * @throws Resources.NotFoundException Throws NotFoundException if the given ID does not exist.
 */
@CheckResult fun Resources.getDrawableCompat(
    @DrawableRes resId: Int,
    theme: Resources.Theme? = null,
): Drawable? =
    ResourcesCompat.getDrawable(this, resId, theme)

/**
 * @see ResourcesCompat.getColor
 *
 * @throws Resources.NotFoundException Throws NotFoundException if the given ID does not exist.
 */
@ColorInt
@CheckResult
fun Resources.getColorCompat(@ColorRes resId: Int, theme: Resources.Theme? = null) =
    ResourcesCompat.getColor(this, resId, theme)

/**
 * @see ResourcesCompat.getColorStateList
 *
 * @throws Resources.NotFoundException Throws NotFoundException if the given ID does not exist.
 */
@CheckResult fun Resources.getColorStateListCompat(
    @ColorRes resId: Int,
    theme: Resources.Theme? = null,
) = ResourcesCompat.getColorStateList(this, resId, theme)

/**
 * @see ResourcesCompat.getColor
 * @see ColorStateList.valueOf
 *
 * @throws Resources.NotFoundException Throws NotFoundException if the given ID does not exist.
 */
@CheckResult fun Resources.getColorStateListFromColor(
    @ColorRes resId: Int,
    theme: Resources.Theme? = null,
) = ColorStateList.valueOf(ResourcesCompat.getColor(this, resId, theme))

/**
 * Request system id of status bar height for this orientation
 * and get dimension in a pixel size
 *
 * @see Resources.getIdentifier
 * @see Resources.getDimensionPixelSize
 * @see Resources.ID_NULL
 *
 * @return dimension in a pixel size or null
 */
@get:Px @get:CheckResult
inline val Resources.statusBarPx: Int?
    get() {
        @SuppressLint("InternalInsetResource", "DiscouragedApi")
        val id = getIdentifier("status_bar_height", "dimen", "android")
        return if (id == ResourcesCompat.ID_NULL) null else getDimensionPixelSize(id)
    }

@Px @CheckResult
fun Resources.dpToPx(@Dp dp: Float): Int = displayMetrics.dpToPx(dp)

@Dp @CheckResult
fun Resources.pxToDp(@Px px: Int): Float = displayMetrics.pxToDp(px)
