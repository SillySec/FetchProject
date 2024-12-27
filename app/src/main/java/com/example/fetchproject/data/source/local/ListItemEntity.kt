package com.example.fetchproject.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fetchproject.data.repo.RepoListItem

/**
 * Class to represent the ListItems received from the API
 *
 * Unsure if [id] is guaranteed to be unique here, so making a localId to guarantee uniqueness and handle
 * duplicate entries if [id] happens to be non-unique
 */
@Entity(tableName = "listItems")
data class ListItemEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Int = 0,
    val id: Int,
    val listId: Int,
    val name: String?
) {
    /**
     * Convert the [ListItemEntity] to a [RepoListItem]
     */
    fun toRepoListItem(): RepoListItem {
        return RepoListItem(
            id = id,
            listId = listId,
            name = name
        )
    }
}

/**
 * Convenience extension function to convert a [RepoListItem] to a [ListItemEntity]
 */
fun RepoListItem.toEntity() : ListItemEntity {
    return ListItemEntity(
        id = id,
        listId = listId,
        name = name
    )

}
