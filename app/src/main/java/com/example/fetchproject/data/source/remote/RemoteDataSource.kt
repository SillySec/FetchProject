package com.example.fetchproject.data.source.remote

import com.example.fetchproject.data.repo.RepoListItem

/**
 * Interface for our RemoteDataSource to implement
 */
interface RemoteDataSource {
    suspend fun getRemoteListItems(): List<RepoListItem>
}