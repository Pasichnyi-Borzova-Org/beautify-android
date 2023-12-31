package com.opasichnyi.beautify.presentation.entity

import com.opasichnyi.beautify.domain.entity.AppointmentStatus
import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.domain.entity.UserRole

data class UIAppointment(
    val id: Long,
    val title: String,
    val master: UserAccount,
    val client: UserAccount,
    val loggedInUserRole: UserRole,
    val date: String,
    val startTime: String,
    val endTime: String,
    val price: String,
    val status: AppointmentStatus,
    val rating: Int? = null,
    val description: String?,
) {
    val partnerUserName = if (loggedInUserRole == UserRole.MASTER) {
        client.username
    } else {
        master.username
    }
}