package com.opasichnyi.beautify.view.base

import android.app.Activity
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.CheckResult
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.opasichnyi.beautify.BuildConfig
import com.opasichnyi.beautify.presentation.base.viewmodel.BaseViewModel
import com.opasichnyi.beautify.presentation.impl.entity.UIEvent
import com.opasichnyi.beautify.util.extension.flow.collectWithLifecycleOwner
import com.opasichnyi.beautify.util.extension.flow.flowWithLifecycleOwner
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Base interface for fragments and activities. Contains common methods.
 */
interface BaseView<VB : ViewBinding, VM : BaseViewModel> : LifecycleOwner {

    /**
     * Must be called after view binding was created. For example within activity
     * in onCreate method or within fragment in onViewCreated method
     *
     * @param binding instance of created view binding
     * @param savedInstanceState bundle that contains last UI saved state
     */
    fun onViewBound(binding: VB, savedInstanceState: Bundle?) {
        // Override if need and make other initialization
    }

    /**
     * [ViewBinding] from [Fragment] or [Activity] or throw Exception
     */
    @CheckResult fun requireBinding(): VB

    /**
     * [Fragment.getViewLifecycleOwner] or [Activity]
     */
    @CheckResult fun getViewLifecycleOwner(): LifecycleOwner

    /**
     * Within this method ViewModel listening must be implemented.
     *
     * @param viewModel instance of [BaseViewModel]
     */
    @CallSuper fun listenViewModel(viewModel: VM) {
        viewModel.unexpectedErrorEvent.launchCollectWithView(
            oneTimeEvent = true,
            errorCollector = ::handleError,
            resultCollector = { _, _ -> throw UnsupportedOperationException() },
        )
    }

    /**
     * Must be used for handling of ViewModel exceptions.
     *
     * @param binding instance of created view binding.
     * @param throwable throwable that came from ViewModel.
     */
    fun handleError(binding: VB, throwable: Throwable) {
        Timber.e(throwable)
        if (BuildConfig.DEBUG) {
            TODO("Override and implement error handling logic")
        }
    }

    /**
     * Subscribes to an observer that will be alive while the component/class is alive.
     *
     * Do not use this fun with Adapters that contain Views, as this will cause memory leaks.
     */
    fun <T> SharedFlow<T>.launchCollect(
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        resultObserver: suspend (T) -> Unit,
    ): Job =
        this@BaseView.lifecycleScope.launch {
            collect(
                minActiveState = minActiveState,
                resultObserver = resultObserver,
            )
        }

    /**
     * Subscribes to an observer that will be alive while the View is alive.
     */
    fun <T> SharedFlow<T>.launchCollectWithView(
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        resultObserver: suspend (binding: VB, value: T) -> Unit,
    ): Job =
        getViewLifecycleOwner().lifecycleScope.launch {
            collectWithView(
                minActiveState = minActiveState,
                resultObserver = resultObserver,
            )
        }

    /**
     * Subscribes to an observer that will be alive while the component/class is alive.
     *
     * Do not use this fun with Adapters that contain Views, as this will cause memory leaks.
     */
    suspend fun <T> SharedFlow<T>.collect(
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        resultObserver: suspend (T) -> Unit,
    ): FlowCollector<T> =
        FlowCollector(resultObserver).also { collector ->
            collectWithLifecycleOwner(
                lifecycleOwner = this@BaseView,
                minActiveState = minActiveState,
                collector = collector,
            )
        }

    /**
     * Subscribes to an observer that will be alive while the View is alive.
     */
    suspend fun <T> SharedFlow<T>.collectWithView(
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        resultObserver: suspend (binding: VB, value: T) -> Unit,
    ): FlowCollector<T> {
        val binding = requireBinding()
        return FlowCollector<T> { resultObserver(binding, it) }.also { collector ->
            collectWithLifecycleOwner(
                lifecycleOwner = getViewLifecycleOwner(),
                minActiveState = minActiveState,
                collector = collector,
            )
        }
    }

    /**
     * Simple version [androidx.lifecycle.flowWithLifecycle]
     */
    fun <T> SharedFlow<T>.flowWithLifecycle(
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    ): Flow<T> = flowWithLifecycleOwner(this@BaseView, minActiveState)

    /**
     * Simple version [androidx.lifecycle.flowWithLifecycle] with [getViewLifecycleOwner]
     */
    fun <T> SharedFlow<T>.flowWithLifecycleView(
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    ): Flow<T> = flowWithLifecycleOwner(getViewLifecycleOwner(), minActiveState)

    /**
     * Subscribes to an observer that will be alive while the component/class is alive.
     *
     * Do not use this fun with Adapters that contain Views, as this will cause memory leaks.
     *
     * @param oneTimeEvent Use true to handle the event once.
     * Using true as the default leads to an extension conflict,
     * because of which, using this extension implies that you will always define this option.
     * @param errorCollector General errors WILL NOT be automatically redirected to this listener,
     * as the [CoroutineExecutor][com.opasichnyi.beautify.presentation.base.CoroutineExecutor] knows nothing
     * about the listener you are working with.
     *
     * @param resultCollector [FlowCollector.emit]
     *
     * @return Use this [FlowCollector] to unsubscribe.
     */
    fun <T> SharedFlow<UIEvent<T>>.launchCollect(
        oneTimeEvent: Boolean,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        errorCollector: suspend (Throwable) -> Unit = {},
        resultCollector: suspend (T) -> Unit,
    ): Job =
        this@BaseView.lifecycleScope.launch {
            collect(
                oneTimeEvent = oneTimeEvent,
                minActiveState = minActiveState,
                errorCollector = errorCollector,
                resultCollector = resultCollector,
            )
        }

    /**
     * Subscribes to a collector that will be alive while the View is alive.
     */
    fun <T> SharedFlow<UIEvent<T>>.launchCollectWithView(
        oneTimeEvent: Boolean,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        errorCollector: suspend (binding: VB, t: Throwable) -> Unit = { _, _ -> },
        resultCollector: suspend (binding: VB, value: T) -> Unit,
    ): Job =
        getViewLifecycleOwner().lifecycleScope.launch {
            collectWithView(
                oneTimeEvent = oneTimeEvent,
                minActiveState = minActiveState,
                errorCollector = errorCollector,
                resultCollector = resultCollector,
            )
        }

    /**
     * Subscribes to an observer that will be alive while the component/class is alive.
     *
     * Do not use this fun with Adapters that contain Views, as this will cause memory leaks.
     *
     * @param oneTimeEvent Use true to handle the event once.
     * Using true as the default leads to an extension conflict,
     * because of which, using this extension implies that you will always define this option.
     * @param errorCollector General errors WILL NOT be automatically redirected to this listener,
     * as the [CoroutineExecutor][com.opasichnyi.beautify.presentation.base.CoroutineExecutor] knows nothing
     * about the listener you are working with.
     *
     * @param resultCollector [FlowCollector.emit]
     *
     * @return Use this [FlowCollector] to unsubscribe.
     */
    suspend fun <T> SharedFlow<UIEvent<T>>.collect(
        oneTimeEvent: Boolean,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        errorCollector: suspend (Throwable) -> Unit = {},
        resultCollector: suspend (T) -> Unit,
    ): FlowCollector<UIEvent<T>> =
        FlowCollector<UIEvent<T>> { event ->
            emitOfCollector(
                event = event,
                oneTimeEvent = oneTimeEvent,
                errorCollector = errorCollector,
                resultCollector = resultCollector,
            )
        }.also { collector ->
            collectWithLifecycleOwner(
                lifecycleOwner = this@BaseView,
                minActiveState = minActiveState,
                collector = collector,
            )
        }

    /**
     * Subscribes to an collector that will be alive while the View is alive.
     */
    suspend fun <T> SharedFlow<UIEvent<T>>.collectWithView(
        oneTimeEvent: Boolean,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        errorCollector: suspend (binding: VB, t: Throwable) -> Unit = { _, _ -> },
        resultCollector: suspend (binding: VB, value: T) -> Unit,
    ): FlowCollector<UIEvent<T>> {
        val binding = requireBinding()
        return FlowCollector<UIEvent<T>> { event ->
            emitOfCollector(
                event = event,
                oneTimeEvent = oneTimeEvent,
                errorCollector = { errorCollector(binding, it) },
                resultCollector = { resultCollector(binding, it) },
            )
        }.also { collector ->
            collectWithLifecycleOwner(
                lifecycleOwner = getViewLifecycleOwner(),
                minActiveState = minActiveState,
                collector = collector,
            )
        }
    }

    /**
     * Generalized function for [collect] and [collectWithView]
     */
    private suspend fun <T> emitOfCollector(
        event: UIEvent<T>,
        oneTimeEvent: Boolean,
        errorCollector: suspend (Throwable) -> Unit,
        resultCollector: suspend (T) -> Unit,
    ) {
        event.getDataOrNull(forceResult = !oneTimeEvent)?.let { resultCollector(it) }
            ?: event.getErrorOrNull(forceResult = !oneTimeEvent)?.let { errorCollector(it) }
    }

    companion object {

        const val GENERIC_VIEW_BINDING_POSITION = 0
        const val GENERIC_VIEW_MODEL_POSITION = 1
    }
}
