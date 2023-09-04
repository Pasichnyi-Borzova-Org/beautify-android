package com.opasichnyi.beautify.presentation.entity

import com.opasichnyi.beautify.domain.entity.UserRole

data class UIAppointment(
    val title: String,
    val partnerUsername: String,
    val loggedInUserRole: UserRole,
    val startTime: String,
    val endTime: String,
    val price: String,
    val description: String
)