package com.example.br_flickr.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.example.br_flickr.model.Photo

//room calls
@Dao
interface FlickrDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photo: Photo?)

    @Delete
    fun delete(photo: Photo?)

    @Query("SELECT * FROM photos ORDER BY id COLLATE NOCASE ASC")
    fun allBookmarkPosts(): DataSource.Factory<Int, Photo>

    @Query("SELECT * FROM photos WHERE id = :id")
    fun findId(id: String?): Photo?
}