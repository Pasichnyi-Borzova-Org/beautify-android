package com.opasichnyi.beautify.presentation.impl.entity

import com.opasichnyi.beautify.di.qualifier.execution.Computation
import com.opasichnyi.beautify.presentation.base.entity.PresentationDataDelegate
import com.opasichnyi.beautify.presentation.base.observable.ProgressObservable
import com.opasichnyi.beautify.presentation.base.observable.adapter.RootDataObservableAdapter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject

/**
 * This data class is intended to extend the base presentation of a class.
 * Example, for: [ViewModel][androidx.lifecycle.ViewModel] or
 * [Presenter](https://en.wikipedia.org/wiki/Model–view–presenter).
 *
 * Where you can use to Presenter?
 * - The Presenter can be used for
 * [Foreground Services](https://developer.android.com/guide/components/foreground-services).
 *
 * Examples of additional extensions of [PresentationDataEntity]:
 * - Access to the navigation router to navigate through the app;
 * - Access to the repository or interactor to logout
 * in the event of an expired authorization token;
 * - etc. for [BaseViewModel][com.opasichnyi.beautify.presentation.base.viewmodel.BaseViewModel] or
 * BasePresenter.
 */
open class PresentationDataEntity @Inject constructor(
    @Computation override val backgroundDispatcher: CoroutineDispatcher,
    override val coroutineExceptionHandler: CoroutineExceptionHandler,
    override val rootDataObservable: RootDataObservableAdapter,
    override val progressObservable: ProgressObservable,
) : PresentationDataDelegate
