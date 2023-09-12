package com.opasichnyi.beautify.data.entity

data class DataAppointment(
    val id: Long,
    val title: String,
    val masterUserName: String,
    val clientUserName: String,
    val startTime: String,
    val endTime: String,
    val price: Double,
    val description: String,
)