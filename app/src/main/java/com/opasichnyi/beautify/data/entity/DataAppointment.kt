package com.opasichnyi.beautify.data.entity

import android.os.Parcelable
import com.opasichnyi.beautify.domain.entity.UserAccount
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataAppointment(
    val id: Long,
    val title: String,
    val master: UserAccount,
    val client: UserAccount,
    val startTime: String,
    val endTime: String,
    val price: Double,
    val description: String,
) : Parcelable