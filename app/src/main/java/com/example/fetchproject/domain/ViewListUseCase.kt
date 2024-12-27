package com.example.fetchproject.domain

import com.example.fetchproject.data.repo.FetchRepo
import com.example.fetchproject.data.repo.RepoListItem
import com.example.fetchproject.data.repo.RepoResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to read the current list items from the database
 *
 * set it up with invoke() so we can treat the class as a function
 */
class ViewListUseCase @Inject constructor(private val repo: FetchRepo) {
    operator fun invoke() : Flow<RepoResult<List<RepoListItem>>> {
        return repo.getListFlow()
    }
}