package com.opasichnyi.beautify.domain.entity

sealed class AppointmentCreationResult {

    data class Success(
        val appointment: Appointment,
    ) : AppointmentCreationResult()

    data class Error(val reason: ErrorReason) : AppointmentCreationResult()
}

enum class ErrorReason {
    TIME_BUSY,
}