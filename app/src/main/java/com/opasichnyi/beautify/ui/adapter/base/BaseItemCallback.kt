package com.opasichnyi.beautify.ui.adapter.base

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

abstract class BaseItemCallback<T : Any> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem === newItem

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
}
