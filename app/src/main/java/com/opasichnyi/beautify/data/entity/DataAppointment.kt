package com.opasichnyi.beautify.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.opasichnyi.beautify.domain.entity.UserAccount
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataAppointment(
    val id: Long,
    val title: String,
    @SerializedName("master")
    val master: DataUserAccount,
    @SerializedName("client")
    val client: DataUserAccount,
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("end_time")
    val endTime: String,
    val price: Double,
    val description: String?,
) : Parcelable