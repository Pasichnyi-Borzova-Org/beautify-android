package com.opasichnyi.beautify.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.lenovo.smartoffice.common.util.extension.lifecycle.repeatOnStart
import com.opasichnyi.beautify.databinding.FragmentUsersListBinding
import com.opasichnyi.beautify.presentation.viewmodel.UsersListViewModel
import com.opasichnyi.beautify.ui.adapter.users.UsersListAdapter
import com.opasichnyi.beautify.ui.base.BaseFragment
import com.opasichnyi.beautify.util.ext.navigateActionSafe

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
        val searchView =
            binding?.searchView

        searchView?.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.filterUsers(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.filterUsers(query)
                return true
            }

        })
    }

    override fun listenViewModel(
        viewModel: UsersListViewModel,
        binding: FragmentUsersListBinding
    ) {
        super.listenViewModel(viewModel, binding)

        repeatOnStart {
            viewModel.usersFlow.collect { list ->
                adapter.submitList(list)
                binding.usersRecyclerView.adapter = adapter
            }
        }

        repeatOnStart {
            viewModel.selectedUserFlow.collect { user ->
                val action =
                    UsersListFragmentDirections.actionUsersFragmentToUserDetailsFragment(
                        user
                    )
                findNavController().navigateActionSafe(action)
            }
        }
    }

}