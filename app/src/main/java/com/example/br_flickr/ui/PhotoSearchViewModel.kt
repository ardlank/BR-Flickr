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

    val photos = switchMap(currentPosts) { posts -> posts.pagedList }
    val networkState = switchMap(currentPosts) {  posts -> posts.networkState }
    val refreshState = switchMap(currentPosts) {  posts -> posts.refreshState }

    init {
        currentPosts.addSource(currentSearchQuery) { query ->
            currentPosts.value = photoRepo.postsOfPhoto(query)
        }
        currentPosts.addSource(currentRepo) {repo ->
            when(repo){
                FlickrPostSource.Type.NETWORK -> {
                    val query = currentSearch()
                    if(query is String) currentPosts.value = photoRepo.postsOfPhoto(query)
                }
                FlickrPostSource.Type.DB -> currentPosts.value = photoRepoDB.postsOfPhoto()
            }
        }
    }

    fun refresh() = currentPosts.value?.refresh?.invoke()

    fun retry() {
        val listing = currentPosts.value
        listing?.retry?.invoke()
    }

    fun setRepo(isNetwork: Boolean) {
        if (isNetwork) currentRepo.value = FlickrPostSource.Type.NETWORK
        else currentRepo.value = FlickrPostSource.Type.DB
    }

    fun showSearch(searchQuery: String): Boolean {
        if(currentSearch() == searchQuery) {
            return false
        }
        currentSearchQuery.value = searchQuery.toUpperCase()
        return true
    }

    fun currentSearch(): String? = currentSearchQuery.value
}