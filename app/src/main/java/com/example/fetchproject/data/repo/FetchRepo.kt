package com.example.fetchproject.data.repo

import kotlinx.coroutines.flow.Flow

/**
 * Interface that our Repo will follow
 */
interface FetchRepo {
    /**
     * Returns the Flow of [RepoResult] containing the list of [RepoListItems]
     */
    fun getListFlow(): Flow<RepoResult<List<RepoListItem>>>

    /**
     * Tell the repo to get the latest list from the Remote API, returns a [RepoResult]
     */
    suspend fun refreshList() : RepoResult<Unit>
}