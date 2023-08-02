package com.opasichnyi.beautify.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.opasichnyi.beautify.presentation.impl.observable.RootDataObservableImpl
import com.opasichnyi.beautify.presentation.impl.observable.RootDataObservableImpl.RootDataValue
import com.opasichnyi.beautify.util.TestActivity
import com.opasichnyi.beautify.util.TestConfig
import com.opasichnyi.beautify.view.impl.entity.RootDataUIEntity
import com.opasichnyi.beautify.view.impl.entity.RootDataUIEntity.Title
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import kotlin.reflect.KClass

/* ktlint-disable max-line-length */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = TestConfig.MANIFEST, sdk = [TestConfig.SDK_VERSION])
class RootDataObservableTest {

    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var rootDataObservable: RootDataObservableImpl

    private lateinit var rootDataMap: Map<KClass<out RootDataUIEntity>, RootDataValue<*>>

    private inline val RootDataUIEntity.key get() = this::class

    @Before
    fun setUp() {
        rootDataObservable = RootDataObservableImpl()
        rootDataMap = rootDataObservable.rootDataMap
    }

    // RootDataObservable.post Start

    @Test
    fun `test that data does not exists`() {
        assertTrue(rootDataMap.values.none { it.stateFlow.value != null })
        assertTrue(rootDataMap.values.none { it.oldData != null })
    }

    @Test
    fun `test that data does exists and rootDataObservable store it`() {
        val testData = Title("Test title")

        assertTrue(rootDataObservable.emit(testData))

        assertEquals(rootDataMap.values.count { it.stateFlow.value != null }, 1)
        assertEquals(testData, rootDataMap[testData.key]?.stateFlow?.value)
        assertEquals(testData, rootDataMap[testData.key]?.oldData)
    }

    @Test
    fun `test that data exists and rootDataObservable replace old data when data concater is null`() {
        val testDataOne = Title("Test title one")
        val testDataTwo = Title("Test title two")

        assertTrue(rootDataObservable.emit(testDataOne))
        assertTrue(rootDataObservable.emit(testDataTwo))

        assertEquals(rootDataMap.values.count { it.stateFlow.value != null }, 1)
        assertEquals(testDataTwo, rootDataMap[testDataTwo.key]?.stateFlow?.value)
        assertEquals(testDataTwo, rootDataMap[testDataTwo.key]?.oldData)
    }

    @Test
    fun `test that new data equals to old data and rootDataObservable post data with distinct and do nothing`() {
        val testDataOne = Title("Test title")
        val testDataTwo = Title("Test title")

        val controller: ActivityController<*> =
            Robolectric.buildActivity(TestActivity::class.java).create().start()

        rootDataObservable.launchCollect(controller.get() as TestActivity) {
            assertNotNull(it)
            assertTrue(it === testDataOne)
        }

        assertTrue(rootDataObservable.emit(testDataOne))
        assertFalse(rootDataObservable.emit(testDataTwo))

        assertEquals(rootDataMap.values.count { it.stateFlow.value != null }, 1)
    }

    // RootDataObservable.post End

    // RootDataObservable.observe Start

    @Test
    fun `test that data will be received when observe`() {
        val testData = Title("Test title")

        val controller: ActivityController<*> =
            Robolectric.buildActivity(TestActivity::class.java).create().start()

        rootDataObservable.launchCollect(controller.get() as TestActivity) {
            assertNotNull(it)
            assertTrue(it === testData)
        }

        assertTrue(rootDataObservable.emit(testData))
    }

    // RootDataObservable.observe End
}
