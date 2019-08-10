package com.example.br_flickr.source

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.example.br_flickr.util.NetworkState

//Data wrapper for liveData and handling
data class Posts<T>(
    // the LiveData of paged lists for the UI to observe
    val pagedList: LiveData<PagedList<T>>? = null,

    // represents the network request status to show to the user
    val networkState: LiveData<NetworkState>? = null,

    //Used to see if we should refresh
    val refreshState: LiveData<NetworkState>? = null,

    // refreshes the whole data and fetches it from scratch.
    val refresh: () -> Unit = {},

    // retries any failed requests.
    val retry: () -> Unit = {}
)