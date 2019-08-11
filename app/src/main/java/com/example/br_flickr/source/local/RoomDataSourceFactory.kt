package com.example.br_flickr.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.example.br_flickr.model.Photo
import com.example.br_flickr.util.NetworkState

class RoomDataSourceFactory(private val flickrDB: FlickrDB,
                            private val config: PagedList.Config) {

    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()

    fun refreshRoom() {
        getRoomPhotos()
    }

    fun getRoomPhotos() : LiveData<PagedList<Photo>> {
        networkState.value = NetworkState.LOADED
        initialLoad.value = NetworkState.LOADED
        return flickrDB.posts().allPosts().toLiveData(config)
    }

}