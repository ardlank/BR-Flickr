package com.example.br_flickr.source.local

import androidx.paging.DataSource
import com.example.br_flickr.model.PhotoObject
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class FlickrDatabase private constructor(
    private val flickrDao: FlickrDao) {

    suspend fun bookmarkPhoto(photo: PhotoObject?) {
        withContext(IO) {
            flickrDao.insert(photo)
        }
    }

    suspend fun removeBookmark(photo: PhotoObject?) {
        withContext(IO) {
            flickrDao.delete(photo)
        }
    }

    fun photoInRepo(id: String?) = flickrDao.findId(id)

    fun getAllBookmarks(): DataSource.Factory<Int, PhotoObject> {
        return flickrDao.allBookmarkPosts()
    }

    //Future work: Implement search
    fun getQueryBookmarks(query: String): DataSource.Factory<Int, PhotoObject> {
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