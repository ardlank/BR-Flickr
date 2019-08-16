package com.example.br_flickr.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.example.br_flickr.model.Photo
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class FlickrDatabase private constructor(
    private val flickrDao: FlickrDao) {

    suspend fun bookmarkPhoto(photo: Photo?) {
        withContext(IO) {
            flickrDao.insert(photo)
        }
    }

    suspend fun removeBookmark(photo: Photo?) {
        withContext(IO) {
            flickrDao.delete(photo)
        }
    }

    fun photoInRepo(id: String?) = flickrDao.findId(id)

    fun getAllBookmarks(): DataSource.Factory<Int, Photo> {
        return flickrDao.allBookmarkPosts()
    }

    //Future work: Implement search
    fun getQueryBookmarks(query: String): DataSource.Factory<Int, Photo> {
        return flickrDao.allBookmarkPosts()
    }

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: FlickrDatabase? = null

        fun getInstance(flickrDao: FlickrDao) =
            instance ?: synchronized(this) {
                instance ?: FlickrDatabase(flickrDao).also { instance = it }
            }
    }
}