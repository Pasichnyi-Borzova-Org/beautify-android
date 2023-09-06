package com.opasichnyi.beautify.presentation.mapper

import com.opasichnyi.beautify.domain.entity.Appointment
import com.opasichnyi.beautify.presentation.entity.UIAppointment
import java.text.SimpleDateFormat
import java.util.Locale

class DomainAppointmentToUIAppointmentMapper {

    private val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.US)
    private val timeFormat = SimpleDateFormat(TIME_FORMAT, Locale.US)


    fun mapDomainAppointmentToUI(appointment: Appointment): UIAppointment {
        return UIAppointment(
            id = appointment.id,
            title = appointment.title,
            partnerUsername = appointment.partnerUsername,
            loggedInUserRole = appointment.loggedInUserRole,
            date = dateFormat.format(appointment.startTime),
            startTime = timeFormat.format(appointment.startTime),
            endTime = timeFormat.format(appointment.endTime),
            // TODO("Change dollar sign to different currencies (BTF-12)")
            price = "${appointment.price}$",
            description = appointment.description,
        )
    }

    companion object {
        const val DATE_FORMAT = "dd MMM yyyy"
        const val TIME_FORMAT = "hh:mm"
    }
}