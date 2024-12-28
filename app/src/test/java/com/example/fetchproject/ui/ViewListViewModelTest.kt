package com.example.fetchproject.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.fetchproject.data.repo.RepoListItem
import com.example.fetchproject.data.repo.RepoResult
import com.example.fetchproject.domain.RefreshListUseCase
import com.example.fetchproject.domain.ViewListUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ViewListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private val viewListUseCase: ViewListUseCase = mockk()
    private val refreshListUseCase: RefreshListUseCase = mockk()
    private lateinit var viewModel: ViewListViewModel

    private val mockItems = listOf(
        RepoListItem(
            id = 1,
            listId = 1,
            name = "Item 1"
        ),
        RepoListItem(
            id = 2,
            listId = 2,
            name = "Item 2"
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `viewmodel init - initial state is Loading`() = runTest {
        coEvery { refreshListUseCase() } returns RepoResult.Success(Unit)
        coEvery { viewListUseCase() } returns flowOf(RepoResult.Success(mockItems))

        viewModel = ViewListViewModel(viewListUseCase, refreshListUseCase)

        viewModel.uiState.test {
            assertEquals(RepoResult.Loading, awaitItem())
            assertEquals(RepoResult.Success(mockItems), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refreshList happy path - RefreshListUseCase and ViewListUseCase Success`() = runTest {
        coEvery { refreshListUseCase() } returns RepoResult.Success(Unit)
        coEvery { viewListUseCase() } returns flowOf(RepoResult.Success(mockItems))

        viewModel = ViewListViewModel(viewListUseCase, refreshListUseCase)

        // Let init go through
        advanceUntilIdle()

        viewModel.uiState.test {
            // Skip current state
            skipItems(1)

            viewModel.refreshList()

            assertEquals(RepoResult.Loading, awaitItem())
            assertEquals(RepoResult.Success(mockItems), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refreshList Error - RefreshListUseCase Error`() = runTest {
        val errorResult = RepoResult.Error.Network(Exception("error"))
        coEvery { refreshListUseCase() } returns errorResult
        coEvery { viewListUseCase() } returns flowOf(RepoResult.Success(mockItems))

        viewModel = ViewListViewModel(viewListUseCase, refreshListUseCase)

        // Let init go through
        advanceUntilIdle()

        viewModel.uiState.test {
            // Skip current state
            skipItems(1)

            viewModel.refreshList()

            assertEquals(RepoResult.Loading, awaitItem())
            assertEquals(errorResult, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refreshList Error - ViewListUseCase Error`() = runTest {
        val errorResult = RepoResult.Error.Database(Exception("error"))
        coEvery { refreshListUseCase() } returns RepoResult.Success(Unit)
        coEvery { viewListUseCase() } returns flowOf(errorResult)

        viewModel = ViewListViewModel(viewListUseCase, refreshListUseCase)

        // Let init go through
        advanceUntilIdle()

        viewModel.uiState.test {
            // Skip current state
            skipItems(1)

            viewModel.refreshList()

            assertEquals(RepoResult.Loading, awaitItem())
            assertEquals(errorResult, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}