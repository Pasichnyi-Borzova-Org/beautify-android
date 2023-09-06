package com.opasichnyi.beautify.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

// TODO("Make sealed if some differences between incoming and outcoming appointments")
@Parcelize
data class Appointment(
    val id: Long,
    val title: String,
    val partnerUsername: String,
    val loggedInUserRole: UserRole,
    val startTime: Date,
    val endTime: Date,
    // TODO("Add different currencies (BTF-12)")
    val price: Double,
    val description: String
) : Parcelable