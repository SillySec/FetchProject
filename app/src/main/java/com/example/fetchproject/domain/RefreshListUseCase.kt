package com.example.fetchproject.domain

import com.example.fetchproject.data.repo.FetchRepo
import com.example.fetchproject.data.repo.RepoResult
import javax.inject.Inject

/**
 * Use case to call the api to refresh our list items
 *
 * set it up with invoke() so we can treat the class as a function
 */
class RefreshListUseCase @Inject constructor(private val repo: FetchRepo) {
    suspend operator fun invoke(): RepoResult<Unit> {
        return repo.refreshList()
    }
}