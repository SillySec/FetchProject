package com.example.fetchproject.data.repo

import com.example.fetchproject.data.source.local.LocalDataSource
import com.example.fetchproject.data.source.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Concrete implentation of the [FetchRepo], uses [LocalDataSource] and [RemoteDataSource] to get listItems
 */
class DefaultFetchRepo @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : FetchRepo {
    override fun getListFlow(): Flow<RepoResult<List<RepoListItem>>> = flow {
        // set Loading
        emit(RepoResult.Loading)

        try {
            localDataSource.getListItemFlow()
                .map { listItems ->
                    if (listItems.isEmpty()) {
                        // items from local source is empty, either it is empty, or no valid data exists
                        RepoResult.Error.EmptyData
                    } else {
                        // we got some times, return success
                        RepoResult.Success(listItems)
                    }
                }.collect { result -> emit(result) }
        } catch (e: Exception) {
            // database error
            emit(RepoResult.Error.Database(e))
        }
    }


    /**
     * call the [remoteDataSource] to get the items and then save the items with [localDataSource]
     *
     * return [RepoResult.Error.Network] if we run into any exceptions, [RepoResult.Success] otherwise
     */
    override suspend fun refreshList(): RepoResult<Unit> {
        try {
            val remoteItems = remoteDataSource.getRemoteListItems()
            localDataSource.setListItems(remoteItems)
            return RepoResult.Success(Unit)
        } catch (e: Exception) {
            return RepoResult.Error.Network(e)
        }
    }

}