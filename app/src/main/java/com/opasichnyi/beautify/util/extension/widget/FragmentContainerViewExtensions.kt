package com.opasichnyi.beautify.util.extension.widget

import androidx.annotation.CheckResult
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView

@get:CheckResult inline val FragmentContainerView.hasFragment
    get() = getFragment<Fragment?>() != null
