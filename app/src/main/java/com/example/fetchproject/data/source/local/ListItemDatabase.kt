package com.example.fetchproject.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ListItemEntity::class],
    version = 1
)
abstract class ListItemDatabase: RoomDatabase() {

    abstract val listItemDao: ListItemDao
}