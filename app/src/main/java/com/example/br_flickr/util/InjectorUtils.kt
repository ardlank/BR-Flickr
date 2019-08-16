package com.example.br_flickr.util

import android.content.Context
import com.example.br_flickr.source.SourceConstants.FLICKR_DATABASE_NAME
import com.example.br_flickr.source.local.FlickrDao
import com.example.br_flickr.source.local.RoomDatabaseFactory
import com.example.br_flickr.source.local.FlickrDatabase
import com.example.br_flickr.source.remote.ApiFactory
import com.example.br_flickr.source.remote.FlickrApi
import com.example.br_flickr.ui.PhotoViewModelFactory

object InjectorUtils {

    fun getPhotoDao(context: Context): FlickrDao {
        return RoomDatabaseFactory.getInstance(
            context.applicationContext, FLICKR_DATABASE_NAME).posts()
    }

    fun getPhotoApi() : FlickrApi {
        return ApiFactory.flickrApi
    }

    fun getFlickrDatabase(context: Context) : FlickrDatabase {
        return FlickrDatabase.getInstance(getPhotoDao(context))
    }

    fun providePhotoViewModelFactory(context: Context):
            PhotoViewModelFactory {
        val db = getFlickrDatabase(context)
        val network = getPhotoApi()
        return PhotoViewModelFactory(network, db)
    }
}