package com.dekola.githublogin.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dekola.githublogin.api.GithubSearchService
import com.dekola.githublogin.data.source.GithubSearchPagingSource
import com.dekola.githublogin.data.source.NETWORK_PAGE_SIZE
import com.dekola.githublogin.model.GithubSearchItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GithubSearchRepository @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val githubSearchService: GithubSearchService,
) : IGithubSearchRepository {

    override suspend fun githubSearch(searchText: String): Flow<PagingData<GithubSearchItem>> {
        return withContext(ioDispatcher) {
            Pager(
                config = PagingConfig(
                    pageSize = NETWORK_PAGE_SIZE,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    GithubSearchPagingSource(githubSearchService, searchText)
                }
            ).flow
        }
    }

    override fun onCleared() {
        ioDispatcher.cancel()
    }
}