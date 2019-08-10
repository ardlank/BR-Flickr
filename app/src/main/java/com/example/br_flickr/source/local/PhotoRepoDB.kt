package com.example.br_flickr.source.local

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.example.br_flickr.model.Photo
import com.example.br_flickr.source.Posts
import com.example.br_flickr.source.SourceConstants
import com.example.br_flickr.source.remote.FlickrPostSource
import com.example.br_flickr.util.NetworkState
import java.util.concurrent.Executor

//Returns a posts class that loads data directly from Room. Uses Paging
class PhotoRepoDB (private val flickrDB: FlickrDB) : FlickrPostSource {
    @MainThread
    override fun postsOfPhoto(): Posts<Photo> {

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(SourceConstants.FLICKR_PAGE_SIZE)
            .setInitialLoadSizeHint(SourceConstants.FLICKR_PAGE_SIZE * 2)
            .build()

        val livePagedList = flickrDB.posts().allPosts().toLiveData(pagedListConfig)

        return Posts(
            pagedList = livePagedList
        )
    }
}