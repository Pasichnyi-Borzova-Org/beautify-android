package com.opasichnyi.beautify.view.base.dialog

import androidx.appcompat.app.AppCompatDialogFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Base class for injection dependencies to all
 * [DialogFragments][com.opasichnyi.beautify.view.base.dialog.BaseDialog]s.
 * Define your injectable dependencies here.
 * If you want to inject specific dependency to specific fragment - just define your
 * dependency and annotate your specific fragment with [AndroidEntryPoint] too.
 */
@AndroidEntryPoint abstract class InjectableDialog : AppCompatDialogFragment()
