package com.opasichnyi.beautify.view.base.fragment

import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Base class for injection dependencies to all fragments. Define your injectable dependencies here.
 * If you want to inject specific dependency to specific fragment - just define your
 * dependency and annotate your specific fragment with [AndroidEntryPoint] too.
 */
@AndroidEntryPoint abstract class InjectableFragment : Fragment() {

    @Inject lateinit var onBackPressedDispatcher: OnBackPressedDispatcher
}
