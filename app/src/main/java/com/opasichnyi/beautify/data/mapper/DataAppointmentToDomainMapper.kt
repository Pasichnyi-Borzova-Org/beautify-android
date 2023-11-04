package com.opasichnyi.beautify.data.mapper

import com.opasichnyi.beautify.data.entity.DataAppointment
import com.opasichnyi.beautify.data.exception.MappingDataToDomainException
import com.opasichnyi.beautify.domain.entity.Appointment
import com.opasichnyi.beautify.domain.entity.UserRole

class DataAppointmentToDomainMapper(
    private val dateMapper: DateMapper,
) {

    fun mapDataAppointmentToDomain(
        dataAppointment: DataAppointment,
        loggedInUsername: String,
    ): Appointment {

        require(dataAppointment.clientUserName != dataAppointment.masterUserName)

        val (partnerUsername, currentUserRole) = when (loggedInUsername) {
            dataAppointment.clientUserName -> {
                Pair(dataAppointment.masterUserName, UserRole.CLIENT)
            }

            dataAppointment.masterUserName -> {
                Pair(dataAppointment.clientUserName, UserRole.MASTER)
            }

            else -> {
                throw MappingDataToDomainException()
            }
        }

        return Appointment(
            id = dataAppointment.id,
            title = dataAppointment.title,
            partnerUsername = partnerUsername,
            loggedInUserRole = currentUserRole,
            startTime = dateMapper.mapStringToDate(dataAppointment.startTime),
            endTime = dateMapper.mapStringToDate(dataAppointment.endTime),
            price = dataAppointment.price,
            description = dataAppointment.description,
        )
    }
}