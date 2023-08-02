package com.opasichnyi.beautify.view.stub.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.res.ResourcesCompat
import kotlin.math.max

class SquareFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = ResourcesCompat.ID_NULL,
    @StyleRes defStyleRes: Int = ResourcesCompat.ID_NULL,
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = max(measuredWidth, measuredHeight)
        val measureSpecification = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY)
        super.onMeasure(measureSpecification, measureSpecification)
    }
}
