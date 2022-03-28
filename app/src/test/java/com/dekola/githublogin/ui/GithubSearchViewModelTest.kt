package com.dekola.githublogin.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.dekola.githublogin.R
import com.dekola.githublogin.utils.Network
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GithubSearchViewModelTest {

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var githubLoginViewModel: GithubSearchViewModel

    @MockK
    private lateinit var network: Network

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        MockKAnnotations.init(this, relaxed = true)
        every { network.isConnected() } returns true
        githubLoginViewModel = GithubSearchViewModel(mockk(), network)
    }

    @Test
    fun test_no_internet() {
        every { network.isConnected() } returns false
        githubLoginViewModel.searchText = anyString()
        githubLoginViewModel.githubSearch()
        assertEquals(R.string.no_internet_connection,
            githubLoginViewModel.toastLiveData.value?.getContentIfNotHandled()?.message)
    }

    @Test
    fun test_empty_search_success() {
        githubLoginViewModel.searchText = ""
        githubLoginViewModel.githubSearch()
        assertEquals(R.string.empty_input,
            githubLoginViewModel.toastLiveData.value?.getContentIfNotHandled()?.message)
    }

    @Test
    fun test_empty_search_failed() {
        githubLoginViewModel.searchText = "test"
        githubLoginViewModel.githubSearch()
        assertEquals(null,
            githubLoginViewModel.toastLiveData.value?.getContentIfNotHandled()?.message)
    }

    @Test
    fun test_true_loading_state() {
        val loadState = CombinedLoadStates(LoadState.Loading,
            LoadState.NotLoading(endOfPaginationReached = false),
            LoadState.NotLoading(endOfPaginationReached = false),
            mockk(),
            null)

        githubLoginViewModel.manageLoadStates(loadState)

        assertEquals(true, githubLoginViewModel.loadLiveData.value?.getContentIfNotHandled())
    }

    @Test
    fun test_false_loading_state() {
        val loadState = CombinedLoadStates(LoadState.NotLoading(endOfPaginationReached = false),
            LoadState.NotLoading(endOfPaginationReached = false),
            LoadState.NotLoading(endOfPaginationReached = false),
            mockk(),
            null)

        githubLoginViewModel.manageLoadStates(loadState)

        assertEquals(false, githubLoginViewModel.loadLiveData.value?.getContentIfNotHandled())
    }


    @Test
    fun test_error_prepend_loading_state() {
        val errorMessage = "prepend error message"
//        val loadState2 = CombinedLoadStates(null, null, null, null, null)

        val loadState = CombinedLoadStates(LoadState.NotLoading(endOfPaginationReached = false),
            LoadState.Error(Throwable(errorMessage)),
            LoadState.NotLoading(endOfPaginationReached = false),
            mockk(),
            null)

        githubLoginViewModel.manageLoadStates(loadState)

        assertEquals(errorMessage, githubLoginViewModel.showError.value?.getContentIfNotHandled())
    }


    @Test
    fun test_error_append_loading_state() {
        val errorMessage = "append error message"

        val loadState = CombinedLoadStates(
            LoadState.NotLoading(endOfPaginationReached = false),
            LoadState.NotLoading(endOfPaginationReached = false),
            LoadState.Error(Throwable(errorMessage)),
            mockk(),
            null)

        githubLoginViewModel.manageLoadStates(loadState)

        assertEquals(errorMessage, githubLoginViewModel.showError.value?.getContentIfNotHandled())
    }

    @Test
    fun test_error_refresh_loading_state() {
        val errorMessage = "refresh error message"

        val loadState = CombinedLoadStates(LoadState.Error(Throwable(errorMessage)),
            LoadState.NotLoading(endOfPaginationReached = false),
            LoadState.NotLoading(endOfPaginationReached = false),
            mockk(),
            null)

        githubLoginViewModel.manageLoadStates(loadState)

        assertEquals(errorMessage, githubLoginViewModel.showError.value?.getContentIfNotHandled())
    }

    @After
    fun teardown() {
        unmockkAll()
        Dispatchers.resetMain()
    }
}


