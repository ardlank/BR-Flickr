package com.example.br_flickr.source.remote

import com.example.br_flickr.source.remote.flickr.FlickrApi


//user to create retrofit objects for a base URL
object ApiFactory {
    val flickrApi : FlickrApi = RetrofitFactory.retrofit(ApiConstants.FLICKR_REST_BASE_URL)
        .create(FlickrApi::class.java)
}