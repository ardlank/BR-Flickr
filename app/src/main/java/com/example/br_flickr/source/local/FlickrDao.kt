package com.example.br_flickr.source.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.br_flickr.model.Photo

//room calls
@Dao
interface FlickrDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photo : Photo?)

    @Query("SELECT * FROM photos ORDER BY id COLLATE NOCASE ASC")
    fun allPosts() : DataSource.Factory<Int, Photo>

    @Query("DELETE FROM photos WHERE id = :id")
    fun delete(id: String?)

    @Query("SELECT * FROM photos WHERE id = :id")
    fun findId(id: String?) : Photo?

    @Query("SELECT MAX(indexInResponse) + 1 FROM photos WHERE id = :id")
    fun getNextIndexInPhoto(id: String) : Int
}