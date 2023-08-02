package com.opasichnyi.beautify.view.base.dialog

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Base class for injection dependencies to all
 * [BottomSheetDialogFragments][com.opasichnyi.beautify.view.base.dialog.BaseBottomSheetDialog]s.
 * Define your injectable dependencies here.
 * If you want to inject specific dependency to specific fragment - just define your
 * dependency and annotate your specific fragment with [AndroidEntryPoint] too.
 */
@AndroidEntryPoint abstract class InjectableBottomSheetDialog : BottomSheetDialogFragment()
