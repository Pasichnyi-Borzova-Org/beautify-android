package com.opasichnyi.beautify.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.lenovo.smartoffice.common.util.extension.lifecycle.repeatOnStart
import com.opasichnyi.beautify.databinding.FragmentEventsListBinding
import com.opasichnyi.beautify.presentation.entity.UIAppointment
import com.opasichnyi.beautify.presentation.viewmodel.AppointmentsViewModel
import com.opasichnyi.beautify.ui.adapter.AppointmentsListAdapter
import com.opasichnyi.beautify.ui.base.BaseFragment

class AppointmentsListFragment : BaseFragment<FragmentEventsListBinding, AppointmentsViewModel>() {

    private lateinit var adapter: AppointmentsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adapter = AppointmentsListAdapter(::onOpenAppointmentInfo, requireContext())
        binding?.appointmentsRecyclerView?.adapter = adapter
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun listenViewModel(
        viewModel: AppointmentsViewModel,
        binding: FragmentEventsListBinding
    ) {
        super.listenViewModel(viewModel, binding)

        viewLifecycleOwner.repeatOnStart {
            viewModel.upcomingAppointmentsFlow.collect { list ->
                adapter.submitList(list)
                binding.appointmentsRecyclerView.adapter = adapter
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.loadAppointments()
    }

    private fun onOpenAppointmentInfo(appointment: UIAppointment) {
        // TODO(Add in BTF-10 - Appointment Details Screen)
        Toast.makeText(requireContext(), "open ${appointment.title}", Toast.LENGTH_LONG).show()
    }

}