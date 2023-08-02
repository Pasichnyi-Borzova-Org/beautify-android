package com.opasichnyi.beautify.view.base.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import timber.log.Timber

abstract class BaseAdapter<T : Any>(
    stateRestorationPolicy: StateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY,
) : RecyclerView.Adapter<BaseViewHolder<T, ViewBinding>>() {

    init {
        this.stateRestorationPolicy = stateRestorationPolicy
    }

    private val _currentList: MutableList<T> = mutableListOf()
    val currentList: List<T> get() = _currentList

    fun add(item: T) {
        _currentList.add(item)
        notifyItemInserted(_currentList.lastIndex)
    }

    fun add(itemList: List<T>) {
        _currentList.addAll(itemList)
        notifyItemRangeInserted(_currentList.size - itemList.size, itemList.size)
    }

    fun remove(item: T) {
        val position = _currentList.indexOf(item)
        removeAt(position)
    }

    fun removeAt(position: Int) {
        if (position in 0.._currentList.lastIndex) {
            _currentList.removeAt(position)
            notifyItemRemoved(position)
        } else {
            Timber.d("List doesn't contain such item on position $position")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeAll() {
        _currentList.clear()
        notifyDataSetChanged()
    }

    fun submitItemBy(position: Int, item: T) {
        if (position in 0.._currentList.lastIndex) {
            _currentList[position] = item
            notifyItemChanged(position)
        } else {
            Timber.d("List doesn't contain such position $position")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItemList: List<T>?) {
        _currentList.clear()
        newItemList?.let {
            _currentList.addAll(it)
        }
        notifyDataSetChanged()
    }

    fun getItem(position: Int): T = _currentList[position]

    override fun getItemCount(): Int = _currentList.size

    override fun onBindViewHolder(holder: BaseViewHolder<T, ViewBinding>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewRecycled(holder: BaseViewHolder<T, ViewBinding>) {
        holder.unbind()
        super.onViewRecycled(holder)
    }
}
