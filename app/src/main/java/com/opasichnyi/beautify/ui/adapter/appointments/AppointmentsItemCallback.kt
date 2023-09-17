package com.opasichnyi.beautify.ui.adapter.appointments

import com.opasichnyi.beautify.presentation.entity.UIAppointment
import com.opasichnyi.beautify.ui.adapter.base.BaseItemCallback


class AppointmentsItemCallback : BaseItemCallback<UIAppointment>() {

    override fun areItemsTheSame(
        oldItem: UIAppointment,
        newItem: UIAppointment
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: UIAppointment,
        newItem: UIAppointment
    ): Boolean {
        return oldItem == newItem
    }
}
