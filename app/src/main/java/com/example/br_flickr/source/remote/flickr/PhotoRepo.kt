package com.example.br_flickr.source.remote.flickr

import androidx.annotation.MainThread
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.example.br_flickr.model.Photo
import androidx.paging.PagedList
import com.example.br_flickr.source.SourceConstants
import com.example.br_flickr.source.Posts
import com.example.br_flickr.source.local.FlickrDB
import com.example.br_flickr.source.remote.FlickrPostSource

//Returns a posts class that loads data directly from API. Uses Paging
class PhotoRepo(private val flickrDB: FlickrDB) : FlickrPostSource {

    @MainThread
    override fun postsOfPhoto(searchQuery: String) : Posts<Photo> {
        val sourceFactory = FlickrDataSourceFactory(searchQuery, flickrDB)

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(SourceConstants.FLICKR_PAGE_SIZE)
            .setInitialLoadSizeHint(SourceConstants.FLICKR_PAGE_SIZE * 2)
            .build()

        val livePagedList = LivePagedListBuilder(sourceFactory, pagedListConfig)
            .setInitialLoadKey(1)
            .build()

        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.initialLoad
        }
        val networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.networkState
        }
        return Posts(
            pagedList = livePagedList,
            networkState = networkState,
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