package com.dekola.githublogin.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * This prevents the repetitive use of singleLiveEvent?.getContentIfNotHandled() everytime.
 */
fun <T> LiveData<SingleLiveEvent<T>>.observeContentIfNotHandled(
    owner: LifecycleOwner,
    observer: Observer<T>,
) {
    this.observe(owner) { singleLiveEvent ->
        singleLiveEvent?.getContentIfNotHandled()?.let { content: T ->
            observer.onChanged(content)
        }
    }
}

fun <T> MutableLiveData<T>.asLiveData() = this as LiveData<T>
