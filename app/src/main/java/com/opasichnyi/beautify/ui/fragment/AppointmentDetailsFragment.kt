package com.opasichnyi.beautify.ui.fragment

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lenovo.smartoffice.common.util.extension.lifecycle.repeatOnStart
import com.opasichnyi.beautify.R
import com.opasichnyi.beautify.databinding.FragmentAppointmentDetailsBinding
import com.opasichnyi.beautify.domain.entity.Appointment
import com.opasichnyi.beautify.presentation.viewmodel.AppointmentDetailsViewModel
import com.opasichnyi.beautify.ui.base.BaseFragment

class AppointmentDetailsFragment :
    BaseFragment<FragmentAppointmentDetailsBinding, AppointmentDetailsViewModel>() {

    private val args: AppointmentDetailsFragmentArgs by navArgs()

    private val appointment: Appointment get() = args.appointment

    override fun onResume() {
        super.onResume()
        viewModel.onAppointmentLoaded(appointment)
        binding?.deleteAppointmentButton?.setOnClickListener {
            viewModel.deleteAppointment(appointment)
        }
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
                    userTv.text = appointment.partnerUserName
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

        repeatOnStart {
            viewModel.deletedAppointmentFlow.collect{
                findNavController().popBackStack()
            }
        }
    }
}