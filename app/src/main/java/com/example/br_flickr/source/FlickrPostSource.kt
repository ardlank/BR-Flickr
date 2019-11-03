package com.example.br_flickr.source

import com.example.br_flickr.model.PhotoObject

//interface for pulling photos
interface FlickrPostSource {
    fun postsOfPhoto(searchQuery: String? = null): Posts<PhotoObject> {
        return Posts()
    }

    enum class Type {
        NETWORK,
        DB
    }
}