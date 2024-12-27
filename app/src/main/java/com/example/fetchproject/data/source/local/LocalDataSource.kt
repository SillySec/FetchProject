package com.example.fetchproject.data.source.local

import com.example.fetchproject.data.repo.RepoListItem
import com.example.fetchproject.data.repo.RepoResult
import kotlinx.coroutines.flow.Flow

/**
 * Interface for our local data source
 */
interface LocalDataSource {
    fun getListItemFlow(): Flow<List<RepoListItem>>

    suspend fun setListItems(listItems: List<RepoListItem>)
}