package com.opasichnyi.beautify.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lenovo.smartoffice.common.util.extension.lifecycle.repeatOnStart
import com.opasichnyi.beautify.databinding.FragmentUsersListBinding
import com.opasichnyi.beautify.presentation.viewmodel.UsersListViewModel
import com.opasichnyi.beautify.ui.adapter.users.UsersListAdapter
import com.opasichnyi.beautify.ui.base.BaseFragment

class UsersListFragment : BaseFragment<FragmentUsersListBinding, UsersListViewModel>() {

    private lateinit var adapter: UsersListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adapter = UsersListAdapter(viewModel::onUserSelected, requireContext())
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadUsers()
    }

    override fun listenViewModel(viewModel: UsersListViewModel, binding: FragmentUsersListBinding) {
        super.listenViewModel(viewModel, binding)

        repeatOnStart {
            viewModel.usersFlow.collect{list ->
                adapter.submitList(list)
                binding.usersRecyclerView.adapter = adapter
            }
        }

        repeatOnStart {
            viewModel.selectedUserFlow.collect{user ->
                // TODO("Navigate to user details screen")
            }
        }
    }

}