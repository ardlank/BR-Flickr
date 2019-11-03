package com.example.br_flickr.source.local

import androidx.paging.DataSource
import androidx.room.*
import com.example.br_flickr.model.PhotoObject

//room calls
@Dao
interface FlickrDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photo: PhotoObject?)

    @Delete
    fun delete(photo: PhotoObject?)

    @Query("SELECT * FROM photos ORDER BY id COLLATE NOCASE ASC")
    fun allBookmarkPosts(): DataSource.Factory<Int, PhotoObject>

    @Query("SELECT * FROM photos WHERE id = :id")
    fun findId(id: String?): PhotoObject?
}