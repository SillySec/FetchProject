package com.example.fetchproject.data.repo

/**
 * Class we'll use throughout the app to communicate results from the repo to the UI layer
 */
sealed class RepoResult<out R> {
    data class Success<out T>(val data: T) : RepoResult<T>()
    // to encapsulate all errors
    sealed class Error : RepoResult<Nothing>() {
        data class Network(val exception: Exception) : Error()
        data class Database(val exception: Exception) : Error()
        data object EmptyData : Error()
    }
    data object Loading : RepoResult<Nothing>()
}