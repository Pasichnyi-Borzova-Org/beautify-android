package com.opasichnyi.beautify.ui.fragment

import android.os.Bundle
import com.lenovo.smartoffice.common.util.extension.lifecycle.repeatOnStart
import com.opasichnyi.beautify.R
import com.opasichnyi.beautify.databinding.FragmentAppointmentDetailsBinding
import com.opasichnyi.beautify.domain.entity.Appointment
import com.opasichnyi.beautify.presentation.viewmodel.AppointmentDetailsViewModel
import com.opasichnyi.beautify.ui.base.BaseFragment

class AppointmentDetailsFragment :
    BaseFragment<FragmentAppointmentDetailsBinding, AppointmentDetailsViewModel>() {

    override fun onResume() {
        super.onResume()
        val appointment: Appointment =
            arguments?.getParcelable(KEY_APPOINTMENT) ?: throw NullPointerException()
        viewModel.onAppointmentLoaded(appointment)
    }

    override fun listenViewModel(
        viewModel: AppointmentDetailsViewModel,
        binding: FragmentAppointmentDetailsBinding
    ) {
        super.listenViewModel(viewModel, binding)
        repeatOnStart {
            viewModel.selectedAppointmentFlow.collect { appointment ->
                binding.apply {
                    titleTv.text = appointment.title
                    descriptionTv.text = appointment.description
                    userTv.text = appointment.partnerUsername
                    timeTv.text =
                        getString(
                            R.string.appointment_date_time_format,
                            appointment.date,
                            appointment.startTime,
                            appointment.endTime
                        )
                }
            }
        }
    }

    companion object {

        private const val KEY_APPOINTMENT = "APPOINTMENT"

        fun newInstance(appointment: Appointment): AppointmentDetailsFragment {
            return AppointmentDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_APPOINTMENT, appointment)
                }
            }
        }
    }

}