package com.opasichnyi.beautify.presentation.entity

import com.opasichnyi.beautify.domain.entity.UserRole

data class UIAppointment(
    val id: Long,
    val title: String,
    val partnerUsername: String,
    val loggedInUserRole: UserRole,
    val date: String,
    val startTime: String,
    val endTime: String,
    val price: String,
    val description: String,
)