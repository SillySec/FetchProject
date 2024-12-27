package com.example.fetchproject.data.source.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 * Data access object for the [ListItemEntity] table
 */
@Dao
interface ListItemDao {

    /**
     * observe a list of [ListItemEntity]
     *
     * Requirements are we want only the items that are not null/blank and we want it ordered by
     * [listId] first and then [name]
     */
    @Query("SELECT * FROM listItems WHERE name IS NOT null AND name IS not '' ORDER BY listId, name")
    fun observeListItems(): Flow<List<ListItemEntity>>

    /**
     * Insert or update the given [ListItemEntity]
     */
    @Upsert
    suspend fun upsertListItems(items: List<ListItemEntity>)

    /**
     * Delete all [ListItemEntity]
     */
    @Query("DELETE FROM listItems")
    suspend fun deleteAll()

    /**
     * Delete all [ListItemEntity] and then insert each item from [items]
     */
    @Transaction
    suspend fun setListItems(items: List<ListItemEntity>) {
        deleteAll()
        upsertListItems(items)
    }
}