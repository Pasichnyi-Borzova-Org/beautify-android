package com.opasichnyi.beautify.ui.adapter

import com.opasichnyi.beautify.presentation.entity.UIAppointment


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
