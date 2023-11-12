package com.opasichnyi.beautify.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataUserAccount(
    val username: String,
    val name: String,
    val surname: String?,
    val city: String?,
    @SerializedName("is_master")
    val isMaster: Int,
) : Parcelable