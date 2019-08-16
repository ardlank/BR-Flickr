package com.example.br_flickr.source.remote

import com.example.br_flickr.source.SourceConstants


//user to create retrofit objects for a base URL
object ApiFactory {
    val flickrApi: FlickrApi = RetrofitFactory.retrofit(SourceConstants.FLICKR_REST_BASE_URL)
        .create(FlickrApi::class.java)
}