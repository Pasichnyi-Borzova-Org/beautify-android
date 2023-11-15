package com.opasichnyi.beautify.presentation.viewmodel

import com.opasichnyi.beautify.domain.entity.Appointment
import com.opasichnyi.beautify.domain.interactor.GetUpcomingAppointmentsInteractor
import com.opasichnyi.beautify.presentation.base.BaseViewModel
import com.opasichnyi.beautify.presentation.entity.UIAppointment
import com.opasichnyi.beautify.presentation.mapper.DomainAppointmentToUIAppointmentMapper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppointmentsViewModel(
    private val getUpcomingAppointmentsInteractor: GetUpcomingAppointmentsInteractor,
    private val appointmentMapper: DomainAppointmentToUIAppointmentMapper,
) : BaseViewModel() {

    private val _appointmentsFlow = MutableStateFlow<List<UIAppointment>>(emptyList())
    val appointmentsFlow: StateFlow<List<UIAppointment>> =
        _appointmentsFlow.asStateFlow()

    private val _selectedAppointmentFlow = MutableSharedFlow<Appointment>()
    val selectedAppointmentFlow: SharedFlow<Appointment> =
        _selectedAppointmentFlow.asSharedFlow()

    // TODO("Try to remove force flag and always refresh appointemtns from API?
    //  Retest and decide")
    fun loadAppointments(force: Boolean = false) = scope.launch {
        if (_appointmentsFlow.value.isNotEmpty() && !force) {
            _appointmentsFlow.emit(
                _appointmentsFlow.value
            )
        } else {
            showProgress()
            _appointmentsFlow.emit(
                getUpcomingAppointmentsInteractor()
                    .map(
                        appointmentMapper::mapDomainAppointmentToUI
                    )
            )

            hideProgress()
        }
    }


    fun onAppointmentSelected(appointment: UIAppointment) = scope.launch {
        _selectedAppointmentFlow
            .emit(
                appointmentMapper.mapUIAppointmentToDomain(appointment)
            )
    }
}