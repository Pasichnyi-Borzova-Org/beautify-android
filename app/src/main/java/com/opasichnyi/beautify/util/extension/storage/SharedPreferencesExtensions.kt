package com.opasichnyi.beautify.util.extension.storage

import android.content.SharedPreferences
import androidx.annotation.CheckResult
import androidx.annotation.WorkerThread

/**
 * @see [java.lang.Double.doubleToRawLongBits]
 * @see [SharedPreferences.Editor.putLong]
 */
@WorkerThread
@CheckResult
@Suppress("RedundantSuspendModifier")
suspend fun SharedPreferences.Editor.putDouble(
    key: String,
    value: Double,
): SharedPreferences.Editor =
    putLong(key, java.lang.Double.doubleToRawLongBits(value))

/**
 * @see [java.lang.Double.longBitsToDouble]
 * @see [java.lang.Double.doubleToLongBits]
 * @see [SharedPreferences.getLong]
 */
@WorkerThread
@CheckResult
@Suppress("RedundantSuspendModifier")
suspend fun SharedPreferences.getDouble(key: String, defaultValue: Double) =
    java.lang.Double.longBitsToDouble(
        getLong(key, java.lang.Double.doubleToRawLongBits(defaultValue)),
    )
