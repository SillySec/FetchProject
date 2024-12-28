package com.example.fetchproject.data.source.remote

import com.example.fetchproject.data.network.FetchApiService
import com.example.fetchproject.data.repo.RepoListItem
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class RetrofitRemoteDataSourceTest {
    private val apiService: FetchApiService = mockk()
    private lateinit var remoteDataSource: RetrofitRemoteDataSource

    @Before
    fun setup() {
        remoteDataSource = RetrofitRemoteDataSource(apiService)
    }

    @Test
    fun `getRemoteListItems returns list of RepoItems from apiService`() = runTest {
        val items = listOf(
            RepoListItem(
                id = 1,
                listId = 1,
                name = "Item 1"
            )
        )
        coEvery { apiService.getListItems() } returns items

        val result = remoteDataSource.getRemoteListItems()

        assertEquals(items, result)
    }
}