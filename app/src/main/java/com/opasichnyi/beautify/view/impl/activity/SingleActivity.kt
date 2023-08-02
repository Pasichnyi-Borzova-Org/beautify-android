package com.opasichnyi.beautify.view.impl.activity

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.opasichnyi.beautify.databinding.ActivitySingleBinding
import com.opasichnyi.beautify.presentation.stub.viewmodel.EmptyViewModel
import com.opasichnyi.beautify.view.base.activity.BaseActivity

class SingleActivity : BaseActivity<ActivitySingleBinding, EmptyViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
    }
}
