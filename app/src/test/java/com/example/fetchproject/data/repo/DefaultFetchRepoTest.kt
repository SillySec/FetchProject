package com.example.fetchproject.data.repo

import app.cash.turbine.test
import com.example.fetchproject.data.source.local.LocalDataSource
import com.example.fetchproject.data.source.remote.RemoteDataSource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DefaultFetchRepoTest {
    private val localDataSource: LocalDataSource = mockk()
    private val remoteDataSource: RemoteDataSource = mockk()

    private lateinit var repo: DefaultFetchRepo

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

    private val testException = Exception("test")

    @Before
    fun setup() {
        repo = DefaultFetchRepo(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
        )
    }

    @Test
    fun `getListFlow happy path returns items`() = runTest {
        every { localDataSource.getListItemFlow() } returns flowOf(mockItems)

        repo.getListFlow().test {
            assertEquals(RepoResult.Loading, awaitItem())
            val result = awaitItem()
            assertEquals(RepoResult.Success(mockItems), result)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getListFlow returns EmptyData error when empty list`() = runTest {
        every { localDataSource.getListItemFlow() } returns flowOf(listOf())

        repo.getListFlow().test {
            assertEquals(RepoResult.Loading, awaitItem())
            val result = awaitItem()
            assertEquals(RepoResult.Error.EmptyData, result)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getListFlow returns DB error when exception is thrown`() = runTest {

        every { localDataSource.getListItemFlow() } throws testException

        repo.getListFlow().test {
            assertEquals(RepoResult.Loading, awaitItem())
            val result = awaitItem()
            assertEquals(RepoResult.Error.Database(testException), result)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refreshList happy path returns success`() = runTest {
        coEvery { remoteDataSource.getRemoteListItems() } returns mockItems
        coEvery { localDataSource.setListItems(mockItems) } just Runs

        val result = repo.refreshList()

        assertEquals(RepoResult.Success(Unit), result)
        coVerify { localDataSource.setListItems(mockItems) }
    }

    @Test
    fun `refreshList returns Network Error when remoteDataSource exception`() = runTest {
        coEvery { remoteDataSource.getRemoteListItems() } throws testException
        coEvery { localDataSource.setListItems(mockItems) } just Runs

        val result = repo.refreshList()

        assertEquals(RepoResult.Error.Network(testException), result)
        coVerify(exactly = 0) { localDataSource.setListItems(mockItems) }
    }

    @Test
    fun `refreshList returns Network Error when localDataSource exception`() = runTest {
        coEvery { remoteDataSource.getRemoteListItems() } returns mockItems
        coEvery { localDataSource.setListItems(mockItems) } throws testException

        val result = repo.refreshList()

        assertEquals(RepoResult.Error.Network(testException), result)
        coVerify { localDataSource.setListItems(mockItems) }
    }
}