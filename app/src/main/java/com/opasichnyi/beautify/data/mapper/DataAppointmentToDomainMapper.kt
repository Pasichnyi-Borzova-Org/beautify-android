package com.opasichnyi.beautify.data.mapper

import com.opasichnyi.beautify.data.entity.DataAppointment
import com.opasichnyi.beautify.data.exception.MappingDataToDomainException
import com.opasichnyi.beautify.domain.entity.Appointment
import com.opasichnyi.beautify.domain.entity.UserRole
import com.opasichnyi.beautify.domain.mapper.DateMapper

class DataAppointmentToDomainMapper(
    private val dateMapper: DateMapper,
) {

    fun mapDataAppointmentToDomain(
        dataAppointment: DataAppointment,
        loggedInUsername: String,
    ): Appointment {

        require(dataAppointment.client != dataAppointment.master)

        val currentUserRole = when (loggedInUsername) {
            dataAppointment.client.login -> {
                UserRole.CLIENT
            }

            dataAppointment.master.login -> {
                UserRole.MASTER
            }

            else -> {
                throw MappingDataToDomainException()
            }
        }

        return Appointment(
            id = dataAppointment.id,
            title = dataAppointment.title,
            master = dataAppointment.master,
            client = dataAppointment.client,
            loggedInUserRole = currentUserRole,
            startTime = dateMapper.mapStringToDate(dataAppointment.startTime),
            endTime = dateMapper.mapStringToDate(dataAppointment.endTime),
            price = dataAppointment.price,
            description = dataAppointment.description,
        )
    }

    fun mapDomainAppointmentToData(
        appointment: Appointment,
    ): DataAppointment {
        return DataAppointment(
            id = appointment.id,
            title = appointment.title,
            master = appointment.master,
            client = appointment.client,
            startTime = dateMapper.mapDateToString(appointment.startTime),
            endTime = dateMapper.mapDateToString(appointment.endTime),
            price = appointment.price,
            description = appointment.description,
        )
    }
}