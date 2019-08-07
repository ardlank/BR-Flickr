package com.example.br_flickr.source.remote.flickr

import com.example.br_flickr.model.PhotosResponse
import com.example.br_flickr.source.remote.ApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//Set up API URLs
interface FlickrApi {

    @GET(ApiConstants.FLICKR_SEARCH_ENDPOINT)
    suspend fun search(
        @Query("text") searchQuery: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int = ApiConstants.FLICKR_PAGE_SIZE,
        @Query("extras") extras: String = ApiConstants.FLICKR_PHOTO_URLS
    ): Response<PhotosResponse>
}