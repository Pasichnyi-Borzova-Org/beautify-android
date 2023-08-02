package com.opasichnyi.beautify.presentation

import androidx.lifecycle.ViewModel
import com.opasichnyi.beautify.presentation.base.observable.ProgressObservable
import com.opasichnyi.beautify.presentation.impl.entity.PresentationDataEntity
import com.opasichnyi.beautify.presentation.impl.observable.RootDataObservableAdapterImpl
import com.opasichnyi.beautify.presentation.impl.observable.RootDataObservableImpl
import com.opasichnyi.beautify.util.TestConfig
import com.opasichnyi.beautify.util.TestCoroutineExceptionHandler
import com.opasichnyi.beautify.util.TestViewModel
import io.mockk.MockKAnnotations
import io.mockk.mockk
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = TestConfig.MANIFEST, sdk = [TestConfig.SDK_VERSION])
class BaseViewModelTest : CoroutineScope {

    private val testScheduler = TestCoroutineScheduler()

    private val testDispatcher = StandardTestDispatcher(testScheduler)

    override val coroutineContext =
        testDispatcher + CoroutineName("DefaultTestContext")

    private val testCoroutineExceptionHandler = TestCoroutineExceptionHandler()

    private val presentationData = PresentationDataEntity(
        backgroundDispatcher = testDispatcher,
        coroutineExceptionHandler = testCoroutineExceptionHandler,
        rootDataObservable = RootDataObservableAdapterImpl(RootDataObservableImpl(), mockk()),
        progressObservable = ProgressObservable(),
    )

    private lateinit var viewModel: TestViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        viewModel = TestViewModel(presentationData)
    }

    @Test
    fun `test that coroutineContext is cancelled after onCleared is called`() {
        viewModel.scope.apply {
            assertTrue(isActive)

            ViewModel::class.java.getDeclaredMethod("clear").let { clearFun ->
                clearFun.isAccessible = true
                clearFun.invoke(viewModel)
            }

            assertFalse(isActive)
        }
    }
}
