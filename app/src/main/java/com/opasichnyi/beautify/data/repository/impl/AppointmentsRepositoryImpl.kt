package com.opasichnyi.beautify.data.repository.impl

import com.opasichnyi.beautify.data.datasource.LoggedInUserDatasource
import com.opasichnyi.beautify.data.datasource.remote.RemoteAppointmentsDataSource
import com.opasichnyi.beautify.data.mapper.DataAppointmentToDomainMapper
import com.opasichnyi.beautify.domain.entity.Appointment
import com.opasichnyi.beautify.domain.entity.AppointmentCreationResult
import com.opasichnyi.beautify.domain.entity.ErrorReason
import com.opasichnyi.beautify.domain.repository.AppointmentsRepository
import java.util.Calendar

class AppointmentsRepositoryImpl(
    private val loggedInUserDatasource: LoggedInUserDatasource,
    private val appointmentMapper: DataAppointmentToDomainMapper,
    private val appointmentsDataSource: RemoteAppointmentsDataSource,
    ) : AppointmentsRepository {

    override suspend fun getUpcomingAppointments(): List<Appointment> {
        val loggedInUsername = loggedInUserDatasource.getLoggedInUser()
        val dataList = loggedInUsername
            ?.let { appointmentsDataSource.getAppointmentsOfUser(it) }
            ?: throw NullPointerException("logged in user not found")
        return dataList.map {
            appointmentMapper.mapDataAppointmentToDomain(
                it,
                loggedInUsername
            )
        }.filter {
            // TODO("Make upcoming filtering on BE")
            it.startTime > Calendar.getInstance().time
        }.sortedBy { it.startTime }
    }

    override suspend fun tryAddAppointment(appointment: Appointment): AppointmentCreationResult {
        return if (appointmentsDataSource.tryCreateAppointment(
                appointmentMapper.mapDomainAppointmentToData(
                    appointment
                )
            )
        ) {
            AppointmentCreationResult.Success(appointment)
        } else {
            AppointmentCreationResult.Error(ErrorReason.TIME_BUSY)
        }
    }
}