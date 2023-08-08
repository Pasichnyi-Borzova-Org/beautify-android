package com.opasichnyi.beautify.ui.activity

import android.content.Context
import android.content.Intent
import com.opasichnyi.beautify.databinding.ActivityLoginBinding
import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.presentation.viewmodel.LoginActivityViewModel
import com.opasichnyi.beautify.ui.base.BaseActivity

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginActivityViewModel>() {

    companion object {

        fun newIntent(context: Context): Intent =
            Intent(context, LoginActivity::class.java)
    }
}
