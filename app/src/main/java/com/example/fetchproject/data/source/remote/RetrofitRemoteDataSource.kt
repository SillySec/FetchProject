package com.example.fetchproject.data.source.remote


import com.example.fetchproject.data.network.FetchApiService
import com.example.fetchproject.data.repo.RepoListItem
import javax.inject.Inject

/**
 * concrete implementation of our [RemoteDataSource] using retrofit [FetchApiService]
 */
class RetrofitRemoteDataSource @Inject constructor(
    private val fetchApiService: FetchApiService,
    ): RemoteDataSource {
    override suspend fun getRemoteListItems(): List<RepoListItem> {
        return fetchApiService.getListItems()
    }
}