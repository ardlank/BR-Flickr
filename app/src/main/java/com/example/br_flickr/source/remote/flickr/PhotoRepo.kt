package com.example.br_flickr.source.remote.flickr

import androidx.annotation.MainThread
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.example.br_flickr.model.Photo
import androidx.paging.PagedList
import com.example.br_flickr.source.remote.ApiConstants
import com.example.br_flickr.source.remote.Posts

//Returns a posts class that loads data directly from API. Uses Paging
class PhotoRepo {

    @MainThread
    fun postsOfPhoto(searchQuery: String) : Posts<Photo> {
        val sourceFactory = FlickrDataSourceFactory(searchQuery)

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(ApiConstants.FLICKR_PAGE_SIZE)
            .setInitialLoadSizeHint(ApiConstants.FLICKR_PAGE_SIZE * 2)
            .build()

        val livePagedList = LivePagedListBuilder(sourceFactory, pagedListConfig)
            .setInitialLoadKey(1)
            .build()

        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.initialLoad
        }
        return Posts(
            pagedList = livePagedList,
            networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                it.networkState
            },
            retry = {
                sourceFactory.sourceLiveData.value?.retryAllFailed()
            },
            refresh = {
                sourceFactory.sourceLiveData.value?.invalidate()
            },
            refreshState = refreshState
        )
    }
}