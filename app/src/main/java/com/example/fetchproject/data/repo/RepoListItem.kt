package com.example.fetchproject.data.repo

/**
 * Data object our app will use to store items from the api
 */
data class RepoListItem(
    val id: Int,
    val listId: Int,
    val name: String?
)
