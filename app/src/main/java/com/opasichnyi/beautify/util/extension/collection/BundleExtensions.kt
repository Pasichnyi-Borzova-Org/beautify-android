package com.opasichnyi.beautify.util.extension.collection

import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.CheckResult
import java.io.Serializable

@CheckResult
inline fun <reified T : Parcelable> Bundle.getParcelableCompat(name: String?): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(name, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getParcelable(name)
    }

@CheckResult
inline fun <reified T : Serializable> Bundle.getSerializableCompat(name: String?): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(name, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getSerializable(name) as T?
    }

@CheckResult fun Bundle.bundleSizeInBytes(): Int {
    val parcel = Parcel.obtain()

    try {
        parcel.writeBundle(this)
        return parcel.dataSize()
    } finally {
        parcel.recycle()
    }
}
