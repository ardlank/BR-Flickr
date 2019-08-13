package com.example.br_flickr.source.local

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.br_flickr.model.Photo
import com.example.br_flickr.source.Posts
import com.example.br_flickr.source.SourceConstants
import com.example.br_flickr.source.FlickrPostSource
import com.example.br_flickr.util.NetworkState

//Returns a posts class that loads data directly from Room. Uses Paging
class PhotoRepoDB(private val flickrDatabase: FlickrDatabase) : FlickrPostSource {

    private val networkState = MutableLiveData<NetworkState>()
    private val initialLoad = MutableLiveData<NetworkState>()

    @MainThread
    override fun postsOfPhoto(searchQuery: String?): Posts<Photo> {

        return Posts(
            pagedList = if(searchQuery == null) getAllRoomPhotos()
            else getQueryRoomPhotos(searchQuery),
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

    private fun refreshRoom() {
        networkState.value = NetworkState.LOADED
        initialLoad.value = NetworkState.LOADED
    }

    private fun getAllRoomPhotos(): LiveData<PagedList<Photo>> {
        return LivePagedListBuilder(flickrDatabase.getAllBookmarks(), SourceConstants.FLICKR_PAGE_SIZE)
            .build()
    }

    //Future work: Implement search
    private fun getQueryRoomPhotos(query: String): LiveData<PagedList<Photo>> {
        return LivePagedListBuilder(flickrDatabase.getQueryBookmarks(query), SourceConstants.FLICKR_PAGE_SIZE)
            .build()
    }
}