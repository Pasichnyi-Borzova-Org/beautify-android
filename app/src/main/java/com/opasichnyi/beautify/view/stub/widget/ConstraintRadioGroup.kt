package com.opasichnyi.beautify.view.stub.widget

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.autofill.AutofillValue
import android.widget.CompoundButton
import android.widget.RadioButton
import androidx.annotation.AttrRes
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import com.opasichnyi.beautify.util.extension.content.autofillManager
import timber.log.Timber
import kotlin.reflect.jvm.jvmName

class ConstraintRadioGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = ResourcesCompat.ID_NULL,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val innerOnCheckedChangeListener: CompoundButton.OnCheckedChangeListener =
        CompoundButton.OnCheckedChangeListener { childView, checked ->
            if (!checked) throw UnsupportedOperationException()
            onChecked(childView as RadioButton)
        }

    var onCheckedChangeListener: ((CompoundButton) -> Unit)? = null

    val checkedRadioButton get() = children.firstOrNull { it is RadioButton && it.isChecked }

    @get:IdRes val checkedRadioButtonId get() = checkedRadioButton?.id

    init {
        // RadioGroup is important by default, unless app developer overrode attribute.
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1 &&
            importantForAutofill == View.IMPORTANT_FOR_AUTOFILL_AUTO
        ) {
            importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_YES
        }
    }

    override fun onViewAdded(childView: View?) {
        super.onViewAdded(childView)

        if (childView is RadioButton) {
            childView.setOnCheckedChangeListener(innerOnCheckedChangeListener)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && childView.isChecked) {
                context.autofillManager?.notifyValueChanged(this)
            }
        }
    }

    override fun getAccessibilityClassName(): String = ConstraintRadioGroup::class.jvmName

    private fun onChecked(view: RadioButton) {
        children
            .asSequence()
            .filter { it.id != view.id }
            .mapNotNull { it as? RadioButton }
            .filter { it.isChecked }
            .forEach {
                it.setOnCheckedChangeListener(null)
                it.isChecked = false
                it.setOnCheckedChangeListener(innerOnCheckedChangeListener)
            }
        onCheckedChangeListener?.invoke(view)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.autofillManager?.notifyValueChanged(this)
        }
    }

    fun clearCheck() {
        check(View.NO_ID)
    }

    fun check(@IdRes buttonId: Int) {
        if (buttonId == View.NO_ID) {
            children
                .asSequence()
                .mapNotNull { it as? RadioButton }
                .filter { it.isChecked }
                .forEach {
                    it.setOnCheckedChangeListener(null)
                    it.isChecked = false
                    it.setOnCheckedChangeListener(innerOnCheckedChangeListener)
                }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.autofillManager?.notifyValueChanged(this)
            }
        } else {
            findViewById<RadioButton?>(buttonId)?.let(::check)
                ?: Timber.e("Id $buttonId not found")
        }
    }

    fun check(view: RadioButton) {
        view.isChecked = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun autofill(value: AutofillValue) {
        if (!isEnabled) {
            return
        }

        if (!value.isList) {
            Timber.w("$value could not be autofilled into $this")
            return
        }

        val index = value.listValue
        val child = getChildAt(index)

        if (child == null) {
            Timber.w("RadioGroup.autoFill(): no child with index $index")
            return
        }

        check(child.id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAutofillType(): Int =
        if (isEnabled) View.AUTOFILL_TYPE_LIST else View.AUTOFILL_TYPE_NONE

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAutofillValue(): AutofillValue? {
        if (!isEnabled) {
            return null
        }

        val checkedRadioButtonId = checkedRadioButtonId

        return children
            .indexOfFirst { it.id == checkedRadioButtonId }
            .takeIf { it != -1 }
            ?.let(AutofillValue::forList)
    }
}
