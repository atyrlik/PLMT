package me.atyrlik.plmtalexandre.ui.main

import me.atyrlik.plmtalexandre.repository.FlowRepository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: MainViewModel
    private lateinit var repository: FlowRepository


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        repository = mock()
        viewModel = MainViewModel(
            repository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `startRegisteringMeasure should call registerFlowMeasures on repository`() = runBlockingTest {
        doReturn(Unit).`when`(repository).registerFlowMeasures()

        viewModel.startRegisteringMeasure()

        verify(repository).registerFlowMeasures()
    }

    @Test
    fun `viewModel should subscribe to repository measures when created`() = runBlockingTest {
        verify(repository).getMeasures()
    }

}