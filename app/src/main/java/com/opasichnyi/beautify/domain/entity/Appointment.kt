package com.opasichnyi.beautify.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

// TODO("Make sealed if some differences between master and client appointments")
@Parcelize
data class Appointment(
    val id: Long,
    val title: String,
    val client: UserAccount,
    val master: UserAccount,
    val loggedInUserRole: UserRole,
    val startTime: Date,
    val endTime: Date,
    // TODO("Add different currencies (BTF-12)")
    val price: Double,
    val status: AppointmentStatus,
    val rating: Int? = null,
    val description: String?,
) : Parcelable

enum class AppointmentStatus {
    CANNOT_COMPLETE, CAN_COMPLETE, COMPLETED
}