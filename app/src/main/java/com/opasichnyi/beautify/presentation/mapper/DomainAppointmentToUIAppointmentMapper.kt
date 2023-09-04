package com.opasichnyi.beautify.presentation.mapper

import com.opasichnyi.beautify.domain.entity.Appointment
import com.opasichnyi.beautify.presentation.entity.UIAppointment
import java.text.SimpleDateFormat

class DomainAppointmentToUIAppointmentMapper {
    private val dateFormat = SimpleDateFormat.getDateTimeInstance()


    fun mapDomainAppointmentToUI(appointment: Appointment): UIAppointment {
        return UIAppointment(
            title = appointment.title,
            partnerUsername = appointment.partnerUsername,
            loggedInUserRole = appointment.loggedInUserRole,
            startTime = dateFormat.format(appointment.startTime),
            endTime = dateFormat.format(appointment.endTime),
            // TODO("Change dollar sign to different currencies (BTF-12)")
            price = "${appointment.price}$",
            description = appointment.description,
        )
    }
}