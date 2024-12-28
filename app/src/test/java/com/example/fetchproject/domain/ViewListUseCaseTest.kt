package com.example.fetchproject.domain

import app.cash.turbine.test
import com.example.fetchproject.data.repo.FetchRepo
import com.example.fetchproject.data.repo.RepoListItem
import com.example.fetchproject.data.repo.RepoResult
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class ViewListUseCaseTest {
    private val repo: FetchRepo = mockk()
    private lateinit var useCase: ViewListUseCase

    @Before
    fun setup() {
        useCase = ViewListUseCase(repo)
    }

    @Test
    fun `invoke returns repo result with list of items`() = runTest {
        val items = listOf(
            RepoListItem(
                id = 1,
                listId = 1,
                name = "Item 1"
            )
        )
        val result = RepoResult.Success(items)
        every { repo.getListFlow() } returns flowOf(result)

        val useCaseResult = useCase()

        useCaseResult.test {
            assertEquals(result, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

}