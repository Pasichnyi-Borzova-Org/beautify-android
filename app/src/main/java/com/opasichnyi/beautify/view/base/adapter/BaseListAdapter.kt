package com.opasichnyi.beautify.view.base.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy
import androidx.viewbinding.ViewBinding
import com.opasichnyi.beautify.view.stub.adapter.diffutil.DefaultDiffUtilItemCallback
import java.util.Objects

/**
 * @see ListAdapter
 * @see StateRestorationPolicy
 *
 * @param areItemsTheSame see [DefaultDiffUtilItemCallback.overrideAreItemsTheSame]
 * @param areContentsTheSame see [DefaultDiffUtilItemCallback.overrideAreContentsTheSame]
 * @param callback see [DefaultDiffUtilItemCallback]
 * @param stateRestorationPolicy see [ListAdapter.setStateRestorationPolicy]
 */
abstract class BaseListAdapter<T : Any>(
    areItemsTheSame: (T, T) -> Boolean = Objects::equals,
    areContentsTheSame: (T, T) -> Boolean = Objects::equals,
    callback: DiffUtil.ItemCallback<T> = DefaultDiffUtilItemCallback(
        overrideAreItemsTheSame = areItemsTheSame,
        overrideAreContentsTheSame = areContentsTheSame,
    ),
    stateRestorationPolicy: StateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY,
) : ListAdapter<T, BaseViewHolder<T, ViewBinding>>(callback) {

    init {
        this.stateRestorationPolicy = stateRestorationPolicy
    }

    public override fun getItem(position: Int): T = super.getItem(position)

    override fun onBindViewHolder(holder: BaseViewHolder<T, ViewBinding>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewRecycled(holder: BaseViewHolder<T, ViewBinding>) {
        holder.unbind()
        super.onViewRecycled(holder)
    }
}
