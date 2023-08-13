package com.opasichnyi.beautify.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.opasichnyi.beautify.databinding.ActivityMainBinding
import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.presentation.viewmodel.MainActivityViewModel
import com.opasichnyi.beautify.ui.base.BaseActivity


class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    private var isNavigated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        splash.setKeepOnScreenCondition { true }
        super.onCreate(savedInstanceState)
        subscribeViewModel()
        viewModel.checkLoggedInUser()
    }

    private fun subscribeViewModel() {
        viewModel.loggedInUserLiveDataAccount.observe(this, ::handleNavigation)
    }

    private fun handleNavigation(userAccount: UserAccount?) {
        Log.e("MainActivity", "handleNavigation")
        isNavigated = true
        if (userAccount == null) {
            showLoginActivity()
        } else {
            showHomeActivity(userAccount)
        }
        finish()
    }

    private fun showLoginActivity() =
        startActivity(LoginActivity.newIntent(this))


    private fun showHomeActivity(userAccount: UserAccount) =
        startActivity(HomeActivity.newIntent(this, userAccount))

}
