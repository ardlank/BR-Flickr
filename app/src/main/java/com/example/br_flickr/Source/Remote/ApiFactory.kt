package com.example.br_flickr.Source.Remote

object ApiFactory {
    val fickerApi : FlickrApi = RetrofitFactory.retrofit(ApiConstants.FLICKR_REST_BASE_URL)
        .create(FlickrApi::class.java)
}