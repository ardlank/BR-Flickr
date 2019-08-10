package com.example.br_flickr.ui

import androidx.lifecycle.*
import com.example.br_flickr.source.remote.flickr.PhotoRepo
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.paging.PagedList
import com.example.br_flickr.model.Photo
import com.example.br_flickr.source.Posts
import com.example.br_flickr.source.local.FlickrDB
import com.example.br_flickr.source.local.PhotoRepoDB
import com.example.br_flickr.source.remote.FlickrPostSource
import com.example.br_flickr.util.NetworkState
import com.google.gson.Gson

//ViewModel of Search View
class PhotoSearchViewModel(flickrDB: FlickrDB)
    : ViewModel() {

    private var photoRepo: PhotoRepo = PhotoRepo(flickrDB)
    private var photoRepoDB: PhotoRepoDB = PhotoRepoDB(flickrDB)

    private val currentSearchQuery = MutableLiveData<String>()
    private val currentRepo = MutableLiveData<FlickrPostSource.Type>()

    var currentPosts = MediatorLiveData<Posts<Photo>>()

    val photos = switchMap(currentPosts) {
//        it.pagedList?.value?.forEach { photo ->
//            if(flickrDB.posts().findId(photo?.id) != null) photo?.isBookmarked = true
//        }
        it.pagedList }
    val networkState = switchMap(currentPosts) { it.networkState }
    val refreshState = switchMap(currentPosts) { it.refreshState }

    init {
        currentPosts.addSource(currentSearchQuery) {
            currentPosts.value = photoRepo.postsOfPhoto(it)
        }
        currentPosts.addSource(currentRepo) {repo ->
            when(repo){
                FlickrPostSource.Type.NETWORK -> currentPosts.value = photoRepo.postsOfPhoto(currentSearch()!!)
                FlickrPostSource.Type.DB -> currentPosts.value = photoRepoDB.postsOfPhoto()
            }
        }
    }


    fun refresh() {
        currentPosts.value?.refresh?.invoke()
    }

    fun retry() {
        val listing = currentPosts.value
        listing?.retry?.invoke()
    }

    fun setRepo(isNetwork: Boolean) {
        if (isNetwork) currentRepo.value = FlickrPostSource.Type.NETWORK
        else currentRepo.value = FlickrPostSource.Type.DB
    }

    fun showSearch(searchQuery: String): Boolean {
        currentSearchQuery.value = searchQuery
        return true
    }

    fun currentSearch(): String? = currentSearchQuery.value
}