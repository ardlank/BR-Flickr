package com.example.br_flickr.source.local

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.br_flickr.model.Photo
import com.example.br_flickr.source.Posts
import com.example.br_flickr.source.SourceConstants
import com.example.br_flickr.source.remote.FlickrPostSource
import com.example.br_flickr.util.NetworkState

//Returns a posts class that loads data directly from Room. Uses Paging
class PhotoRepoDB (private val flickrDatabase: FlickrDatabase) : FlickrPostSource {

    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()

    @MainThread
    override fun postsOfPhoto(): Posts<Photo> {
        return Posts(
            pagedList = getRoomPhotos(),
            retry = {
                refreshRoom()
            },
            refresh = {
                refreshRoom()
            },
            networkState = networkState,
            refreshState = initialLoad
        )
    }

    fun refreshRoom() {
        networkState.value = NetworkState.LOADED
        initialLoad.value = NetworkState.LOADED
    }

    fun getRoomPhotos() : LiveData<PagedList<Photo>> {
        return LivePagedListBuilder(flickrDatabase.getAllPosts(), SourceConstants.FLICKR_PAGE_SIZE)
            .build()
    }
}