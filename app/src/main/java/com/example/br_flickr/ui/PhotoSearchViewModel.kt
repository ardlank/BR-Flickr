package com.example.br_flickr.ui

import androidx.lifecycle.*
import com.example.br_flickr.source.remote.flickr.PhotoRepo
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap

//ViewModel of Search View
class PhotoSearchViewModel: ViewModel() {

    private var photoRepo: PhotoRepo = PhotoRepo()

    private val currentSearchQuery = MutableLiveData<String>()
    private val repoResult = map(currentSearchQuery) { query ->
        photoRepo.postsOfPhoto(query)
    }
    val photos = switchMap(repoResult) { posts -> posts.pagedList }
    val networkState = switchMap(repoResult) { posts -> posts.networkState }
    val refreshState = switchMap(repoResult) { posts -> posts.refreshState }

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun showSearch(searchQuery: String): Boolean {
        if (currentSearchQuery.value == searchQuery) {
            return false
        }
        currentSearchQuery.value = searchQuery
        return true
    }

    fun retry() {
        val listing = repoResult.value
        listing?.retry?.invoke()
    }
}