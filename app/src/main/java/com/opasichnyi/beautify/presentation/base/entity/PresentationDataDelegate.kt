package com.opasichnyi.beautify.presentation.base.entity

import com.opasichnyi.beautify.presentation.base.observable.ProgressObservable
import com.opasichnyi.beautify.presentation.base.observable.adapter.RootDataObservableAdapter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler

/**
 * Interface for a description of a [delegation](https://kotlinlang.org/docs/delegation.html)
 * of extensions for presentation level.
 */
interface PresentationDataDelegate {

    /**
     * [CoroutineDispatcher][kotlinx.coroutines.CoroutineDispatcher]
     * which recommended used for [launch][kotlinx.coroutines.launch] or
     * [launchSafe][com.opasichnyi.beautify.presentation.base.CoroutineExecutor.launchSafe].
     */
    val backgroundDispatcher: CoroutineDispatcher

    /**
     * An object which handles exception thrown by a coroutine.
     */
    val coroutineExceptionHandler: CoroutineExceptionHandler

    /**
     * A more comfortable list of functions to work with
     * [RootDataObservable][com.opasichnyi.beautify.presentation.base.observable.RootDataObservable]
     */
    val rootDataObservable: RootDataObservableAdapter

    /**
     * Allows you to show and hide
     * the [ProgressDialog][com.opasichnyi.beautify.view.stub.dialog.ProgressDialog]
     *
     * See more:
     * [BaseActivity.listenViewModel][com.opasichnyi.beautify.view.base.activity.BaseActivity.listenViewModel]
     * and
     * [ProgressDialog.
     * listenViewModel][com.opasichnyi.beautify.view.stub.dialog.ProgressDialog.listenViewModel]
     */
    val progressObservable: ProgressObservable
}
