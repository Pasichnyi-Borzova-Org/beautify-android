package com.opasichnyi.beautify.util.extension.resources

import android.util.DisplayMetrics
import androidx.annotation.CheckResult
import androidx.annotation.Px
import com.opasichnyi.beautify.util.resource.Dp

@Px @CheckResult
fun DisplayMetrics.dpToPx(@Dp dp: Float): Int =
    (dp * density).toInt()

@Dp @CheckResult
fun DisplayMetrics.pxToDp(@Px px: Int): Float = px / density
