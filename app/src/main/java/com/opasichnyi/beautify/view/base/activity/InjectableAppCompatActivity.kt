package com.opasichnyi.beautify.view.base.activity

import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * Base class for injection dependencies to all activities.
 * Define your injectable dependencies here.
 * If you want to inject specific dependency to specific activity - just define your
 * dependency and annotate your specific activity with [AndroidEntryPoint] too.
 */
@AndroidEntryPoint abstract class InjectableAppCompatActivity : AppCompatActivity()
