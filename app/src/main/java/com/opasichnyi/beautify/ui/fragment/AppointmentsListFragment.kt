package com.opasichnyi.beautify.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lenovo.smartoffice.common.util.extension.lifecycle.repeatOnStart
import com.opasichnyi.beautify.R
import com.opasichnyi.beautify.databinding.FragmentEventsListBinding
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
        adapter = AppointmentsListAdapter(viewModel::onAppointmentSelected, requireContext())
        binding?.appointmentsRecyclerView?.adapter = adapter
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun listenViewModel(
        viewModel: AppointmentsViewModel,
        binding: FragmentEventsListBinding
    ) {
        super.listenViewModel(viewModel, binding)

        repeatOnStart {
            viewModel.upcomingAppointmentsFlow.collect { list ->
                adapter.submitList(list)
                binding.appointmentsRecyclerView.adapter = adapter
            }
        }

        repeatOnStart {
            viewModel.selectedAppointmentFlow.collect { appointment ->
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container, AppointmentDetailsFragment.newInstance(appointment)
                    )
                    .addToBackStack("details")
                    .commit()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.loadAppointments()
    }
}