package com.example.br_flickr.Source.Remote

import com.example.br_flickr.Model.PhotosResponse
import com.example.br_flickr.Source.Remote.ApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {

    @GET(ApiConstants.FLICKR_SEARCH_ENDPOINT)
    suspend fun search(
        @Query("text") text: String,
        @Query("extras") extras: String = ApiConstants.PHOTO_URLS
    ): Response<PhotosResponse>
}