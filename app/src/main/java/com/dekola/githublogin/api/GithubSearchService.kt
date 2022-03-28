package com.dekola.githublogin.api

import com.dekola.githublogin.model.GithubSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubSearchService {

    @GET("search/users")
    suspend fun githubSearch(
        @Query("q") search: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<GithubSearchResponse>
}