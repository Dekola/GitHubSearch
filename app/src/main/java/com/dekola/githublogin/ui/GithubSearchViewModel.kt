package com.dekola.githublogin.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dekola.githublogin.R
import com.dekola.githublogin.data.repository.IGithubSearchRepository
import com.dekola.githublogin.data.wrapper.ToastWrapper
import com.dekola.githublogin.model.GithubSearchItem
import com.dekola.githublogin.utils.NetworkConnectivity
import com.dekola.githublogin.utils.SimpleIdlingResource
import com.dekola.githublogin.utils.SingleLiveEvent
import com.dekola.githublogin.utils.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubSearchViewModel @Inject constructor(
    private val repository: IGithubSearchRepository,
    private val networkConnectivity: NetworkConnectivity,
) : ViewModel() {

    var mIdlingResource: SimpleIdlingResource? = null

    private val _showError = MutableLiveData<SingleLiveEvent<String>>()
    var showError = _showError.asLiveData()

    private val _loadLiveData = MutableLiveData<SingleLiveEvent<Boolean>>()
    var loadLiveData = _loadLiveData.asLiveData()

    private val _toastLiveData = MutableLiveData<SingleLiveEvent<ToastWrapper>>()
    var toastLiveData = _toastLiveData.asLiveData()

    private val _githubSearchLiveData =
        MutableLiveData<SingleLiveEvent<PagingData<GithubSearchItem>>>()
    val githubSearchLiveData = _githubSearchLiveData.asLiveData()

    var searchText = ""

    fun githubSearch() {
        if (!networkConnectivity.isConnected()) {
            _toastLiveData.postValue(SingleLiveEvent(ToastWrapper(R.string.no_internet_connection)))
        } else if (searchText.isBlank()) {
            _toastLiveData.postValue(SingleLiveEvent(ToastWrapper(R.string.empty_input)))
        } else {
            viewModelScope.launch {
                mIdlingResource?.setIdleState(true)
                repository.githubSearch(searchText).cachedIn(viewModelScope).collect{
                    mIdlingResource?.setIdleState(false)
                    _githubSearchLiveData.postValue(SingleLiveEvent(it))
                }
            }
        }
    }

    fun manageLoadStates(loadState: CombinedLoadStates) {
        _loadLiveData.postValue(SingleLiveEvent(loadState.refresh is LoadState.Loading))
        when {
            loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
            loadState.append is LoadState.Error -> loadState.append as LoadState.Error
            loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
            else -> null
        }?.error?.message?.let { errorMessage ->
            _showError.postValue(SingleLiveEvent(errorMessage))
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.onCleared()
    }
}