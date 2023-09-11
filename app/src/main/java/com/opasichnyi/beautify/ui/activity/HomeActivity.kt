package com.opasichnyi.beautify.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.opasichnyi.beautify.R
import com.opasichnyi.beautify.databinding.ActivityHomeBinding
import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.presentation.viewmodel.HomeViewModel
import com.opasichnyi.beautify.ui.base.BaseActivity

class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavigation()
    }

    private fun setupNavigation() {
        val navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

        binding.bottomNavigation.setupWithNavController(navController)
    }

    companion object {

        fun newIntent(context: Context, userAccount: UserAccount): Intent =
            Intent(context, HomeActivity::class.java).apply {
                putExtra("userAccount", userAccount)
            }
    }
}
