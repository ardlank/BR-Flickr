package com.example.br_flickr.source

import com.example.br_flickr.model.Photo
import com.example.br_flickr.source.Posts

//interface for pulling photos
interface FlickrPostSource {
    fun postsOfPhoto(searchQuery: String? = null): Posts<Photo> {
        return Posts()
    }

    enum class Type {
        NETWORK,
        DB
    }
}