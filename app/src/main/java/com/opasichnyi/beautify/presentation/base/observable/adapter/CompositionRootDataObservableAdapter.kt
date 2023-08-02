package com.opasichnyi.beautify.presentation.base.observable.adapter

import android.content.res.Resources
import com.opasichnyi.beautify.presentation.base.observable.WriteRootDataObservable

/**
 * Composition part [RootDataObservableAdapter]
 */
interface CompositionRootDataObservableAdapter : WriteRootDataObservable {

    val resources: Resources
}
