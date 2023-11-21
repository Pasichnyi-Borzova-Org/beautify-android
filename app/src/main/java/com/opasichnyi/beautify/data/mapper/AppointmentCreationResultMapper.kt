package com.opasichnyi.beautify.data.mapper

import com.opasichnyi.beautify.data.entity.ApiCallResult
import com.opasichnyi.beautify.data.entity.DataAppointment
import com.opasichnyi.beautify.domain.entity.AppointmentCreationError
import com.opasichnyi.beautify.domain.entity.AppointmentCreationResult

class AppointmentCreationResultMapper(
    private val appointmentMapper: DataAppointmentToDomainMapper,
) {

    fun mapAppointmentCreationResultToData(appointmentCreationResult: AppointmentCreationResult): ApiCallResult<DataAppointment, AppointmentCreationError> =
        when (appointmentCreationResult) {
//            is RegisterResult.Error -> ApiCallResult(null, registerResult.reason)
//            is RegisterResult.Success -> ApiCallResult(
//                userAccountMapper.mapDomainUserAccountToData(
//                    registerResult.user
//                ), null
//            )
            is AppointmentCreationResult.Error -> ApiCallResult(
                null,
                appointmentCreationResult.reason
            )

            is AppointmentCreationResult.Success -> ApiCallResult(
                appointmentMapper.mapDomainAppointmentToData(
                    appointmentCreationResult.appointment
                ),
                null
            )
        }


    fun mapAppointmentCreationResultToDomain(
        appointmentCreationResult: ApiCallResult<DataAppointment, AppointmentCreationError>,
        loggedInUserName: String,
    ): AppointmentCreationResult =
        if (appointmentCreationResult.data != null && appointmentCreationResult.error == null) {
            AppointmentCreationResult.Success(
                appointmentMapper.mapDataAppointmentToDomain(
                    appointmentCreationResult.data,
                    loggedInUserName,
                )
            )
        } else if (appointmentCreationResult.data == null && appointmentCreationResult.error != null) {
            AppointmentCreationResult.Error(appointmentCreationResult.error)
        } else {
            throw Exception()
        }
}