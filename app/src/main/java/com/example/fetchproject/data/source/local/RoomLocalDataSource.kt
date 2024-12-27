package com.example.fetchproject.data.source.local


import com.example.fetchproject.data.repo.RepoListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Concrete room implementation of the [LocalDataSource] interface using Room
 */
class RoomLocalDataSource(
    private val listItemDao: ListItemDao
) : LocalDataSource {
    /**
     * Return the flow of [RepoListItems]
     */
    override fun getListItemFlow(): Flow<List<RepoListItem>> {
        return listItemDao.observeListItems().map {
           it.map { listItemEntity ->
                listItemEntity.toRepoListItem()
            }
        }
    }

    /**
     * Set the [ListItemEntity] table to the values supplied in [listItems]
     */
    override suspend fun setListItems(listItems: List<RepoListItem>) {
        listItemDao.setListItems(listItems.map { it.toEntity() })
    }
}