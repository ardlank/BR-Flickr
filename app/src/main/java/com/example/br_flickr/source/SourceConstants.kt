package com.example.br_flickr.source

import com.example.br_flickr.BuildConfig

//Source Constants
object SourceConstants {
    const val FLICKR_INITIAL_PAGE_NUMBER = 1
    const val FLICKR_PAGE_SIZE = 10

    //network
    const val FLICKR_REST_BASE_URL = "https://api.flickr.com/"
    const val FLICKR_SEARCH_ENDPOINT = "services/rest/?method=flickr.photos.search"
    const val FLICKR_PHOTO_URLS = "url_sq, url_t, url_s, url_q, url_m, url_n, url_z, url_c, url_l, url_o"
    const val FLICKR_API_KEY = BuildConfig.FLICKR_API_KEY

    const val FLICKR_DEFAULT_SEARCH = "Sports"

    //Database
    const val FLICKR_DATABASE_NAME = "flickr.db"
}