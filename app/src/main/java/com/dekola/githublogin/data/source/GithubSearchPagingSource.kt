package com.dekola.githublogin.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dekola.githublogin.api.GithubSearchService
import com.dekola.githublogin.model.GithubSearchItem
import org.json.JSONObject

const val GITHUB_STARTING_PAGE_INDEX = 1
const val NETWORK_PAGE_SIZE = 9

class GithubSearchPagingSource(
    private val githubSearchService: GithubSearchService,
    private val searchText: String,
) : PagingSource<Int, GithubSearchItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubSearchItem> {
        val position = params.key ?: GITHUB_STARTING_PAGE_INDEX
        return try {
            val response =
                githubSearchService.githubSearch("$searchText in:login", position, params.loadSize)
            val responseBody = response.body()

            if (response.isSuccessful && responseBody != null) {
                val items = responseBody.items
                val nextKey = if (items?.isEmpty() == true) {
                    null
                } else {
                    position + (params.loadSize / NETWORK_PAGE_SIZE)
                }
                items?.let {
                    LoadResult.Page(
                        data = items,
                        prevKey = if (position == GITHUB_STARTING_PAGE_INDEX) null else position - 1,
                        nextKey = nextKey
                    )
                } ?: run {
                    LoadResult.Error(Exception(""))
                }
            } else {
                val responseString = response.errorBody()?.string()
                val errorString = JSONObject(responseString ?: "").getString("message")
                LoadResult.Error(Exception(errorString))
            }
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GithubSearchItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}