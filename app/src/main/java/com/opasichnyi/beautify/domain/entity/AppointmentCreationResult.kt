package com.opasichnyi.beautify.domain.entity

// TODO("Make generic class like in data layer")
sealed class AppointmentCreationResult {

    data class Success(
        val appointment: Appointment,
    ) : AppointmentCreationResult()

    data class Error(val reason: AppointmentCreationError) : AppointmentCreationResult()
}

//enum class AppointmentCreationErrorReason {
//    TIME_IS_OCCUPIED,
//    REQUIRED_FIELDS_ARE_MISSING,
//    DB_INSERT_FAILED,
//}