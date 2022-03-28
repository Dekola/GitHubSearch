package com.dekola.githublogin.data.repository

import androidx.paging.PagingData
import com.dekola.githublogin.model.GithubSearchItem
import kotlinx.coroutines.flow.Flow

interface IGithubSearchRepository {
    suspend fun githubSearch(searchText: String): Flow<PagingData<GithubSearchItem>>
    fun onCleared()
}