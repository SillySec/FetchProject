package com.example.fetchproject.data.source.local

import app.cash.turbine.test
import com.example.fetchproject.data.repo.RepoListItem
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

class RoomLocalDataSourceTest{
    private val dao: ListItemDao = mockk()
    private lateinit var localDataSource: RoomLocalDataSource

    private val mockItems = listOf(
        RepoListItem(
            id = 1,
            listId = 1,
            name = "Item 1"
        )
    )

    private val mockEntities = listOf(
        ListItemEntity(
            id = 1,
            listId = 1,
            name = "Item 1"
        )
    )

    @Before
    fun setup() {
        localDataSource = RoomLocalDataSource(dao)
    }

    @Test
    fun `getListFlow returns repoListItems`() = runTest {
        every { dao.observeListItems() } returns flowOf(mockEntities)

        val result = localDataSource.getListItemFlow()

        result.test {
            assertEquals(mockItems, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `setListItems saves items to db`() = runTest {
        coEvery { dao.setListItems(mockEntities) } just Runs

        localDataSource.setListItems(mockItems)

        coVerify {
            dao.setListItems(mockEntities)
        }
    }
}