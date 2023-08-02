package com.opasichnyi.beautify.view.stub.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil

/**
 * @see DiffUtil.ItemCallback
 *
 * @property overrideAreItemsTheSame see [DiffUtil.ItemCallback.areItemsTheSame]
 * @property overrideAreContentsTheSame see [DiffUtil.ItemCallback.areContentsTheSame]
 */
class DefaultDiffUtilItemCallback<T : Any>(
    val overrideAreItemsTheSame: (T, T) -> Boolean,
    val overrideAreContentsTheSame: (T, T) -> Boolean,
) : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        overrideAreItemsTheSame(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        overrideAreContentsTheSame(oldItem, newItem)
}
