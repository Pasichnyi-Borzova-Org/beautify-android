package com.opasichnyi.beautify.presentation.base.observable

import androidx.annotation.AnyThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.opasichnyi.beautify.util.extension.flow.launchCollectWithLifecycleOwner
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

/**
 * The base implementation assumes that you will use
 * the [BaseViewModel][com.opasichnyi.beautify.presentation.base.viewmodel.BaseViewModel] and
 * the [PresentationDataDelegate][com.opasichnyi.beautify.presentation.base.entity.PresentationDataDelegate].
 * [progress
 * Observable][com.opasichnyi.beautify.presentation.base.entity.PresentationDataDelegate.progressObservable].
 *
 * You can use this observer for your progress and views.
 */
@ActivityRetainedScoped class ProgressObservable @Inject constructor() {

    private val progressStateFlow = MutableStateFlow(false)

    fun launchCollect(owner: LifecycleOwner, onChange: suspend (visibility: Boolean) -> Unit) {
        progressStateFlow.launchCollectWithLifecycleOwner(owner, Lifecycle.State.RESUMED, onChange)
    }

    @AnyThread fun show() {
        progressStateFlow.value = true
    }

    @AnyThread fun hide() {
        progressStateFlow.value = false
    }
}
