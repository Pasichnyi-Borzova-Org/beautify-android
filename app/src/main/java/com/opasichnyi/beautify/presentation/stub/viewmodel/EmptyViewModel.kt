package com.opasichnyi.beautify.presentation.stub.viewmodel

import com.opasichnyi.beautify.presentation.base.entity.PresentationDataDelegate
import com.opasichnyi.beautify.presentation.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * This is a stub for the ViewModel. Recommended for use in the absence of business logic.
 */
@HiltViewModel
class EmptyViewModel @Inject constructor(
    presentationData: PresentationDataDelegate,
) : BaseViewModel(presentationData)
