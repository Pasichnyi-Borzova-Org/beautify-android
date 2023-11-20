package com.opasichnyi.beautify.presentation.mapper

import com.opasichnyi.beautify.domain.entity.Appointment
import com.opasichnyi.beautify.domain.mapper.DateMapper
import com.opasichnyi.beautify.presentation.entity.UIAppointment
import com.opasichnyi.beautify.presentation.exception.DateMappingException
import com.opasichnyi.beautify.presentation.exception.TimeMappingException
import java.text.SimpleDateFormat
import java.util.Locale

class DomainAppointmentToUIAppointmentMapper(
    private val dateMapper: DateMapper,
) {

    private val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.US)
    private val timeFormat = SimpleDateFormat(TIME_FORMAT, Locale.US)
    private val dateTimeFormat = SimpleDateFormat(DATE_TIME_FORMAT, Locale.US)


    fun mapDomainAppointmentToUI(appointment: Appointment): UIAppointment {
        return UIAppointment(
            id = appointment.id,
            title = appointment.title,
            master = appointment.master,
            client = appointment.client,
            loggedInUserRole = appointment.loggedInUserRole,
            date = dateFormat.format(appointment.startTime),
            startTime = timeFormat.format(appointment.startTime),
            endTime = timeFormat.format(appointment.endTime),
            // TODO("Change dollar sign to different currencies (BTF-12)")
            price = "${appointment.price}$",
            rating = appointment.rating,
            status = appointment.status,
            description = appointment.description,
        )
    }

    // TODO("Replace throwing exception with results entities and handling them on UI")
    fun mapUIAppointmentToDomain(appointment: UIAppointment): Appointment {
        try {
            dateFormat.parse(appointment.date)
                ?: throw DateMappingException("Date has wrong format")
        } catch (ex: Exception) {
            throw TimeMappingException("Date has wrong format")
        }
        return Appointment(
            id = appointment.id,
            title = appointment.title,
            master = appointment.master,
            client = appointment.client,
            loggedInUserRole = appointment.loggedInUserRole,
            // TODO("Change exceptions")
            startTime = try {
                dateTimeFormat.parse(appointment.date + " " + appointment.startTime)
                    ?: throw TimeMappingException("'From' time has wrong format")
            } catch (ex: Exception) {
                throw TimeMappingException("'From' time has wrong format")
            },
            endTime = try {
                dateTimeFormat.parse(appointment.date + " " + appointment.endTime)
                    ?: throw TimeMappingException("To time has wrong format")
            } catch (ex: Exception) {
                throw TimeMappingException("To time has wrong format")
            },
            price = appointment.price.removeSuffix("$").toDouble(),
            status = appointment.status,
            rating = appointment.rating,
            description = appointment.description,
        )
    }

    companion object {
        const val DATE_FORMAT = "dd.MM.yyyy"
        const val TIME_FORMAT = "HH:mm"
        const val DATE_TIME_FORMAT = "$DATE_FORMAT $TIME_FORMAT"
    }
}