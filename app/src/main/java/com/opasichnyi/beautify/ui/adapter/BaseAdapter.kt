package com.opasichnyi.beautify.ui.adapter

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


abstract class BaseListAdapter<T : Any, H : BaseListAdapter.BaseViewHolder<T>>
    (itemCallback: BaseItemCallback<T>) :
    ListAdapter<T, H>(itemCallback) {

    override fun onBindViewHolder(holder: H, position: Int) {
        holder.bind(getItem(position))
    }

    abstract class BaseViewHolder<T>(binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        protected var item: T? = null

        @CallSuper
        open fun bind(item: T) {
            this.item = item
        }
    }
}
