package com.example.br_flickr.source.local

import androidx.annotation.MainThread
import androidx.paging.PagedList
import com.example.br_flickr.model.Photo
import com.example.br_flickr.source.Posts
import com.example.br_flickr.source.SourceConstants
import com.example.br_flickr.source.remote.FlickrPostSource

//Returns a posts class that loads data directly from Room. Uses Paging
class PhotoRepoDB (private val flickrDB: FlickrDB) : FlickrPostSource {
    @MainThread
    override fun postsOfPhoto(): Posts<Photo> {

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(SourceConstants.FLICKR_PAGE_SIZE)
            .setInitialLoadSizeHint(SourceConstants.FLICKR_PAGE_SIZE * 2)
            .build()

        val dataFactory = RoomDataSourceFactory(flickrDB, pagedListConfig)

        return Posts(
            pagedList = dataFactory.getRoomPhotos(),
            retry = {
                dataFactory.refreshRoom()
            },
            refresh = {
                dataFactory.refreshRoom()
            },
            networkState = dataFactory.networkState,
            refreshState = dataFactory.networkState
        )
    }
}