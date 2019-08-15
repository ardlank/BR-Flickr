package com.example.br_flickr.util

import android.content.Context
import com.example.br_flickr.source.SourceConstants.FLICKR_DATABASE_NAME
import com.example.br_flickr.source.local.RoomDatabaseFactory
import com.example.br_flickr.source.local.FlickrDatabase
import com.example.br_flickr.ui.PhotoSearchViewModelFactory

object InjectorUtils {

    fun getPhotoDBRepo(context: Context): FlickrDatabase {
        return FlickrDatabase.getInstance(
            RoomDatabaseFactory.getInstance(context.applicationContext, FLICKR_DATABASE_NAME).posts()
        )
    }

    fun providePhotoViewModelFactory(context: Context):
            PhotoSearchViewModelFactory {
        val repository = getPhotoDBRepo(context)
        return PhotoSearchViewModelFactory(repository)
    }
}