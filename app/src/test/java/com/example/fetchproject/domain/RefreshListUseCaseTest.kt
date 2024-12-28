package com.example.fetchproject.domain

import com.example.fetchproject.data.repo.FetchRepo
import com.example.fetchproject.data.repo.RepoResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RefreshListUseCaseTest {
    private val repo: FetchRepo = mockk()
    private lateinit var useCase: RefreshListUseCase

    @Before
    fun setup() {
        useCase = RefreshListUseCase(repo)
    }

    @Test
    fun `invoke returns repository sync result`() = runTest {
        val result = RepoResult.Success(Unit)
        coEvery { repo.refreshList() } returns result

        val useCaseResult = useCase()
        assertEquals(result, useCaseResult)

    }
}