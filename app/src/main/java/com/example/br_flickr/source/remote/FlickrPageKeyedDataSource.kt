package com.example.br_flickr.source.remote

import android.util.Log
import com.example.br_flickr.model.Photo
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.br_flickr.source.SourceConstants
import com.example.br_flickr.source.local.FlickrDatabase
import com.example.br_flickr.util.NetworkState
import com.example.br_flickr.util.NetworkState.Companion.LOADED
import com.example.br_flickr.util.NetworkState.Companion.LOADING
import kotlinx.coroutines.*


//A data source that uses the before/after keys returned in page requests. Before is not used
class FlickrPageKeyedDataSource(
    private val searchQuery: String,
    private val flickrApi: FlickrApi,
    private val flickrDatabase: FlickrDatabase
) : PageKeyedDataSource<Int, Photo>() {

    private val TAG = "FlickrPageKeyedSource"

    //incase of retry
    private var retry: (() -> Any)? = null

    //liveData for network state
    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            it.invoke()
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {}

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
        networkState.postValue(LOADING)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = flickrApi.search(
                    searchQuery = searchQuery,
                    page = params.key
                )
                if (response.isSuccessful) {
                    val photosResponse = response.body()
                    val photos = photosResponse?.photos?.photo ?: emptyList()
                    retry = null
                    networkState.postValue(LOADED)
                    setBookmark(photos)
                    callback.onResult(photos, params.key + 1)
                } else {
                    retry = {
                        loadAfter(params, callback)
                    }
                    networkState.postValue(
                        NetworkState.error("error code: ${response.code()}")
                    )
                }
            } catch (exception: Exception) {
                retry = {
                    loadAfter(params, callback)
                }
                val message = exception.message ?: "unknown error"
                Log.e(TAG, message, exception)
                networkState.postValue(NetworkState.error(message))
            }
        }
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Photo>) {
        networkState.postValue(LOADING)
        initialLoad.postValue(LOADING)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = flickrApi.search(
                    searchQuery = searchQuery,
                    page = 1
                )
                if (response.isSuccessful) {
                    val photosResponse = response.body()
                    val photos = photosResponse?.photos?.photo ?: emptyList()
                    retry = null
                    initialLoad.postValue(LOADED)
                    networkState.postValue(LOADED)
                    setBookmark(photos)
                    callback.onResult(
                        photos,
                        SourceConstants.FLICKR_INITIAL_PAGE_NUMBER,
                        SourceConstants.FLICKR_INITIAL_PAGE_NUMBER + 1
                    )
                } else {
                    retry = {
                        loadInitial(params, callback)
                    }
                    networkState.postValue(
                        NetworkState.error("error code: ${response.code()}")
                    )
                    initialLoad.postValue(
                        NetworkState.error("error code: ${response.code()}")
                    )
                }
            } catch (exception: Exception) {
                retry = {
                    loadInitial(params, callback)
                }
                val message = exception.message ?: "unknown error"
                Log.e("loadInitial", message, exception)
                val error = NetworkState.error(message)
                networkState.postValue(error)
                initialLoad.postValue(error)
            }
        }
    }

    private fun setBookmark(photos: List<Photo>) {
        for (photo in photos) {
            if (flickrDatabase.photoInRepo(photo.id) != null) {
                photo.isBookmarked = true
            }
        }
    }
}