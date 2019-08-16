package com.example.br_flickr.source.remote

import com.example.br_flickr.model.PhotosResponse
import com.example.br_flickr.source.SourceConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//Set up API URLs
interface FlickrApi {

    @GET(SourceConstants.FLICKR_SEARCH_ENDPOINT)
    suspend fun search(
        @Query("text") searchQuery: String,
        @Query("page") page: Int?,
        @Query("per_page") per_page: Int? = SourceConstants.FLICKR_PAGE_SIZE,
        @Query("extras") extras: String? = SourceConstants.FLICKR_PHOTO_URLS,
        @Query("sort") sort: String? = SourceConstants.FLICKR_PHOTO_SORT
    ): Response<PhotosResponse>
}