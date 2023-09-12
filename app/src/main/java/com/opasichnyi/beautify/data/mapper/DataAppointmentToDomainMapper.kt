package com.opasichnyi.beautify.data.mapper

import com.opasichnyi.beautify.data.entity.DataAppointment
import com.opasichnyi.beautify.data.exception.MappingDataToDomainException
import com.opasichnyi.beautify.domain.entity.Appointment
import com.opasichnyi.beautify.domain.entity.UserRole
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DataAppointmentToDomainMapper {

    private val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)

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
            startTime = sdf.parse(dataAppointment.startTime) as Date,
            endTime = sdf.parse(dataAppointment.endTime) as Date,
            price = dataAppointment.price,
            description = dataAppointment.description,
        )
    }
}