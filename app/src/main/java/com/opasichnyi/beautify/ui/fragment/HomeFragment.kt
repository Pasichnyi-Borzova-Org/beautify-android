package com.opasichnyi.beautify.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.opasichnyi.beautify.R
import com.opasichnyi.beautify.databinding.FragmentHomeBinding
import com.opasichnyi.beautify.presentation.viewmodel.HomeViewModel
import com.opasichnyi.beautify.ui.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.bottomNavigation?.setOnItemSelectedListener(navListener)

        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, AppointmentsListFragment()).commit()
    }

    private val navListener = NavigationBarView.OnItemSelectedListener {

        lateinit var selectedFragment: Fragment
        when (it.itemId) {

            R.id.events -> {
                // TODO("
                //  Now navigation to list
                //  When create calendar view - change to fragment,
                //  which is parent to List anc Calendar (with picker for view)
                //  ")
                selectedFragment = AppointmentsListFragment()
            }

            R.id.search -> {
                selectedFragment = SearchFragment()
            }
        }
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, selectedFragment).commit()
        true
    }
}