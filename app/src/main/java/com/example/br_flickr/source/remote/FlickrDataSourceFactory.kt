package com.example.br_flickr.source.remote

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.br_flickr.model.Photo
import com.example.br_flickr.source.local.FlickrDatabase

//Observe last created data source
class FlickrDataSourceFactory(private val searchQuery: String,
                              private val flickrApi: FlickrApi,
                              private val flickrDatabase: FlickrDatabase) :
    DataSource.Factory<Int, Photo>() {

    val sourceLiveData = MutableLiveData<FlickrPageKeyedDataSource>()

    override fun create(): DataSource<Int, Photo> {
        val source = FlickrPageKeyedDataSource(searchQuery, flickrApi, flickrDatabase)
        sourceLiveData.postValue(source)
        return source
    }
}
