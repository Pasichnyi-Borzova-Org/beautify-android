package com.opasichnyi.beautify.data.repository.impl

import com.opasichnyi.beautify.data.datasource.LoggedInUserDatasource
import com.opasichnyi.beautify.data.datasource.mock.MockAppointmentsDataSource
import com.opasichnyi.beautify.data.mapper.DataAppointmentToDomainMapper
import com.opasichnyi.beautify.domain.entity.Appointment
import com.opasichnyi.beautify.domain.repository.AppointmentsRepository
import java.util.Calendar

class AppointmentsRepositoryImpl(
    private val appointmentsDataSource: MockAppointmentsDataSource,
    private val loggedInUserDatasource: LoggedInUserDatasource,

    ) : AppointmentsRepository {

    override suspend fun getUpcomingAppointments(): List<Appointment> {
        val loggedInUsername = loggedInUserDatasource.getLoggedInUser()
        val dataList = loggedInUsername
            ?.let { appointmentsDataSource.getAppointmentsOfUser(it) }
            ?: throw NullPointerException("logged in user not found")
        return dataList.map {
            DataAppointmentToDomainMapper().mapDataAppointmentToDomain(
                it,
                loggedInUsername
            )
        }.filter {
            // Not sure that we need to filter here.
            // Maybe need to get separately upcoming and all from BE
            it.startTime > Calendar.getInstance().time
        }.sortedBy { it.startTime }
    }
}