package com.opasichnyi.beautify.util.ext

import android.text.Editable

val String.Companion.EMPTY: String
    get() = ""

fun String.wrapDoubleQuotes(): String = "\"" + this + "\""

fun Editable?.stringOrEmpty(): String = this?.toString() ?: String.EMPTY