package com.example.fetchproject.data.network

import com.example.fetchproject.data.repo.RepoListItem
import retrofit2.http.GET

/**
 * RetroFit interface for the remote endpoint
 */
interface FetchApiService {
    @GET("hiring.json")
    suspend fun getListItems() : List<RepoListItem>
}