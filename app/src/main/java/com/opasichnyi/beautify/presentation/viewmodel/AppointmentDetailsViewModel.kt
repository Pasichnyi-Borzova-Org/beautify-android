package com.opasichnyi.beautify.presentation.viewmodel

import com.opasichnyi.beautify.domain.entity.Appointment
import com.opasichnyi.beautify.domain.interactor.CompleteAppointmentInteractor
import com.opasichnyi.beautify.domain.interactor.DeleteAppointmentInteractor
import com.opasichnyi.beautify.domain.interactor.RateAppointmentInteractor
import com.opasichnyi.beautify.presentation.base.BaseViewModel
import com.opasichnyi.beautify.presentation.entity.UIAppointment
import com.opasichnyi.beautify.presentation.mapper.DomainAppointmentToUIAppointmentMapper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AppointmentDetailsViewModel(
    private val appointmentMapper: DomainAppointmentToUIAppointmentMapper,
    private val deleteAppointmentInteractor: DeleteAppointmentInteractor,
    private val completeAppointmentInteractor: CompleteAppointmentInteractor,
    private val rateAppointmentInteractor: RateAppointmentInteractor,
) : BaseViewModel() {

    private val _selectedAppointmentFlow = MutableSharedFlow<UIAppointment>()
    val selectedAppointmentFlow: SharedFlow<UIAppointment> = _selectedAppointmentFlow.asSharedFlow()

    private val _deletedAppointmentFlow = MutableSharedFlow<Appointment>()
    val deletedAppointmentFlow: SharedFlow<Appointment> =
        _deletedAppointmentFlow.asSharedFlow()

    private lateinit var currentAppointment: Appointment

    fun onAppointmentLoaded(appointment: Appointment) = scope.launch {
        updateCurrentAppointment(appointment)
    }

    fun deleteCurrentAppointment() = scope.launch {
        deleteAppointmentInteractor(currentAppointment)
        _deletedAppointmentFlow.emit(currentAppointment)
    }

    fun completeCurrentAppointment() = scope.launch {
        updateCurrentAppointment(
            completeAppointmentInteractor(currentAppointment)
        )
    }

    fun rateAppointment(rating: Int) = scope.launch {
        updateCurrentAppointment(
            rateAppointmentInteractor(currentAppointment, rating)
        )
    }

    private fun updateCurrentAppointment(appointment: Appointment) = scope.launch {
        currentAppointment = appointment
        _selectedAppointmentFlow.emit(
            appointmentMapper.mapDomainAppointmentToUI(
                appointment
            )
        )
    }
}