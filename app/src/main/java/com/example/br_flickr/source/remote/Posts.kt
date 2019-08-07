package com.example.br_flickr.source.remote

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.example.br_flickr.util.NetworkState

//Data wrapper for liveData and handling
data class Posts<T>(
    // the LiveData of paged lists for the UI to observe
    val pagedList: LiveData<PagedList<T>>,

    // represents the network request status to show to the user
    val networkState: LiveData<NetworkState>,

    //Used to see if we should refresh
    val refreshState: LiveData<NetworkState>,

    // refreshes the whole data and fetches it from scratch.
    val refresh: () -> Unit,

    // retries any failed requests.
    val retry: () -> Unit)