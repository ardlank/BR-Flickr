package com.example.br_flickr.Source.Remote

import com.example.br_flickr.BuildConfig

object ApiConstants {
    const val FLICKR_REST_BASE_URL = "https://api.flickr.com/"
    const val FLICKR_SEARCH_ENDPOINT = "services/rest/?method=flickr.photos.search"
    const val PHOTO_URLS = "url_sq, url_t, url_s, url_q, url_m, url_n, url_z, url_c, url_l, url_o"
    const val FLICKR_AP_KEY = BuildConfig.FLICKR_API_KEY
}