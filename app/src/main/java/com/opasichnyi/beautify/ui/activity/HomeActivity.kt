package com.opasichnyi.beautify.ui.activity

import android.content.Context
import android.content.Intent
import com.opasichnyi.beautify.databinding.ActivityHomeBinding
import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.presentation.viewmodel.HomeActivityViewModel
import com.opasichnyi.beautify.ui.base.BaseActivity

class HomeActivity : BaseActivity<ActivityHomeBinding, HomeActivityViewModel>() {

    companion object {

        fun newIntent(context: Context, userAccount: UserAccount): Intent =
            Intent(context, HomeActivity::class.java).apply {
                putExtra("userAccount", userAccount)
            }
    }
}
