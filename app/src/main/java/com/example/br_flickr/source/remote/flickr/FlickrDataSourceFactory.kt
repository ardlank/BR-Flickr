package com.example.br_flickr.source.remote.flickr

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.br_flickr.model.Photo
import com.example.br_flickr.source.local.FlickrDatabase
import com.example.br_flickr.source.remote.ApiFactory

//Observe last created data source
class FlickrDataSourceFactory(private val searchQuery: String, private val flickrDatabase: FlickrDatabase)
    : DataSource.Factory<Int, Photo>() {
    
    private val flickrApi = ApiFactory.flickrApi
    val sourceLiveData = MutableLiveData<FlickrPageKeyedDataSource>()
    
    override fun create(): DataSource<Int, Photo> {
        val source = FlickrPageKeyedDataSource(flickrApi, searchQuery, flickrDatabase)
        sourceLiveData.postValue(source)
        return source
    }
}
