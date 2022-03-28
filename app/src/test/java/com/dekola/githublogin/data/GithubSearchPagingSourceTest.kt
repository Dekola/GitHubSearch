package com.dekola.githublogin.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.dekola.githublogin.api.GithubSearchService
import com.dekola.githublogin.data.source.GithubSearchPagingSource
import com.dekola.githublogin.model.GithubSearchItem
import com.dekola.githublogin.model.GithubSearchResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GithubSearchPagingSourceTest {

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var githubSearchService: GithubSearchService

    private lateinit var githubSearchPagingSource: GithubSearchPagingSource

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        MockKAnnotations.init(this, relaxed = true)
        githubSearchService = mockk()
        githubSearchPagingSource = GithubSearchPagingSource(githubSearchService, "test")
    }

    @Test
    fun `when api service response throws exception`() {
        runTest {
            val httpException = HttpException(
                Response.error<GithubSearchResponse>(
                    500, "Test Server Error"
                        .toResponseBody("text/plain".toMediaTypeOrNull())
                )
            )

            coEvery { (githubSearchService.githubSearch(any(), any(), any())) } throws httpException

            val expectedResult = PagingSource.LoadResult.Error<Int, GithubSearchItem>(httpException)

            assertEquals(expectedResult, githubSearchPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 0,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
            )
        }
    }

    @Test
    fun `when api service response runs successfully`() {
        runTest {

            val response =
                GithubSearchResponse(2, false, listOf(GithubSearchItem(), GithubSearchItem()))

            coEvery {
                (githubSearchService.githubSearch(any(),
                    any(),
                    any()))
            } returns Response.success(response)

            val expectedResult = PagingSource.LoadResult.Page(
                data = response.items!!,
                prevKey = null,
                nextKey = 2
            )

            assertEquals(expectedResult, githubSearchPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 10,
                    placeholdersEnabled = false
                )
            )
            )
        }
    }

    @After
    fun teardown() {
        unmockkAll()
        Dispatchers.resetMain()
    }
}

