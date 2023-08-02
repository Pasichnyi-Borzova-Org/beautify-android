package com.opasichnyi.beautify.util.extension.widget

import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.widget.TextView
import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.core.widget.TextViewCompat

/**
 * @see [CharSequence.isEmpty]
 */
@CheckResult fun TextView.isEmpty() = text.isEmpty()

/**
 * Setter - create a new instance [InputFilter.LengthFilter] or change a max value
 * in existing instance
 *
 * Getter - [InputFilter.LengthFilter.getMax] or -1
 *
 * @see [InputFilter.LengthFilter]
 */
@get:MainThread
@set:MainThread
@get:CheckResult
inline var TextView.maxLength: Int
    get() = filters
        .asSequence()
        .mapNotNull { it as? InputFilter.LengthFilter }
        .firstOrNull()
        ?.max
        ?: -1
    set(value) {
        for (i in filters.indices) {
            if (filters[i] is InputFilter.LengthFilter) {
                filters[i] = InputFilter.LengthFilter(value)
                return
            }
        }

        filters = filters.copyOf(filters.size + 1).also { newFilters ->
            newFilters[newFilters.lastIndex] = InputFilter.LengthFilter(value)
        }
    }

/**
 * Setter - [TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds]
 *
 * Getter - [TextViewCompat.getCompoundDrawablesRelative] by index 0
 */
@get:MainThread
@set:MainThread
@get:CheckResult
inline var TextView.drawableStart: Drawable?
    get() = TextViewCompat.getCompoundDrawablesRelative(this).getOrNull(0)
    set(value) {
        val (_, drawableTop, drawableEnd, drawableBottom) =
            TextViewCompat.getCompoundDrawablesRelative(this)
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            value,
            drawableTop,
            drawableEnd,
            drawableBottom,
        )
    }

/**
 * Setter - [TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds]
 *
 * Getter - [TextViewCompat.getCompoundDrawablesRelative] by index 1
 */
@get:MainThread
@set:MainThread
@get:CheckResult
inline var TextView.drawableTop: Drawable?
    get() = TextViewCompat.getCompoundDrawablesRelative(this).getOrNull(1)
    set(value) {
        val (drawableStart, _, drawableEnd, drawableBottom) =
            TextViewCompat.getCompoundDrawablesRelative(this)
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            drawableStart,
            value,
            drawableEnd,
            drawableBottom,
        )
    }

/**
 * Setter - [TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds]
 *
 * Getter - [TextViewCompat.getCompoundDrawablesRelative] by index 2
 */
@get:MainThread
@set:MainThread
@get:CheckResult
inline var TextView.drawableEnd: Drawable?
    get() = TextViewCompat.getCompoundDrawablesRelative(this).getOrNull(2)
    set(value) {
        val (drawableStart, drawableTop, _, drawableBottom) =
            TextViewCompat.getCompoundDrawablesRelative(this)
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            drawableStart,
            drawableTop,
            value,
            drawableBottom,
        )
    }

/**
 * Setter - [TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds]
 *
 * Getter - [TextViewCompat.getCompoundDrawablesRelative] by index 3
 */
@get:MainThread
@set:MainThread
@get:CheckResult
inline var TextView.drawableBottom: Drawable?
    get() = TextViewCompat.getCompoundDrawablesRelative(this).getOrNull(3)
    set(value) {
        val (drawableStart, drawableTop, drawableEnd, _) =
            TextViewCompat.getCompoundDrawablesRelative(this)
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            drawableStart,
            drawableTop,
            drawableEnd,
            value,
        )
    }
