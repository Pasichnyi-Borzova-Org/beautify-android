package com.opasichnyi.beautify.data.repository.impl

import com.opasichnyi.beautify.data.datasource.LoggedInUserDatasource
import com.opasichnyi.beautify.data.datasource.remote.RemoteAppointmentsDataSource
import com.opasichnyi.beautify.data.mapper.DataAppointmentToDomainMapper
import com.opasichnyi.beautify.domain.entity.Appointment
import com.opasichnyi.beautify.domain.entity.AppointmentCreationResult
import com.opasichnyi.beautify.domain.entity.ErrorReason
import com.opasichnyi.beautify.domain.repository.AppointmentsRepository

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
        }.sortedBy { it.startTime }
    }

    // TODO("Get error reason from response")
    override suspend fun tryAddAppointment(appointment: Appointment): AppointmentCreationResult {
        return try {
            AppointmentCreationResult.Success(
                appointmentMapper.mapDataAppointmentToDomain(
                    appointmentsDataSource.tryCreateAppointment(
                        appointmentMapper.mapDomainAppointmentToData(
                            appointment,
                        )
                    ),
                    loggedInUserDatasource.getLoggedInUser() ?: throw Exception("Error")
                )
            )
        } catch (ex: Exception) {
            AppointmentCreationResult.Error(ErrorReason.TIME_BUSY)
        }
    }

    override suspend fun deleteAppointment(appointment: Appointment) {
        appointmentsDataSource.deleteAppointment(
            appointmentMapper.mapDomainAppointmentToData(
                appointment
            )
        )
    }

    override suspend fun completeAppointment(appointment: Appointment): Appointment {
        return loggedInUserDatasource.getLoggedInUser()?.let {
            appointmentMapper.mapDataAppointmentToDomain(
                appointmentsDataSource.completeAppointment(
                    appointmentMapper.mapDomainAppointmentToData(appointment)
                ),
                it
            )
        } ?: throw NullPointerException("logged in user not found")
    }

    override suspend fun rateAppointment(appointment: Appointment, rating: Int): Appointment {
        return loggedInUserDatasource.getLoggedInUser()?.let {
            appointmentMapper.mapDataAppointmentToDomain(
                appointmentsDataSource.rateAppointment(
                    appointmentMapper.mapDomainAppointmentToData(
                        appointment
                    ), rating
                ),
                it
            )
        } ?: throw NullPointerException("logged in user not found")
    }

    // TODO("Get error reason from response")
    override suspend fun updateAppointment(appointment: Appointment): AppointmentCreationResult {
        return try {
            AppointmentCreationResult.Success(
                appointmentMapper.mapDataAppointmentToDomain(
                    appointmentsDataSource.tryUpdateAppointment(
                        appointmentMapper.mapDomainAppointmentToData(
                            appointment,
                        )
                    ),
                    loggedInUserDatasource.getLoggedInUser()
                        ?: throw Exception("Error getting logged in user")
                )
            )
        } catch (ex: Exception) {
            AppointmentCreationResult.Error(ErrorReason.TIME_BUSY)
        }
    }
}