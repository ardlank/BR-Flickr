package com.example.br_flickr.source.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.br_flickr.model.Photo

//Database schema used by the PhotoRepoDB
@Database(
    entities = arrayOf(Photo::class),
    version = 1,
    exportSchema = false
)
abstract class FlickrDB : RoomDatabase() {
    companion object {
        fun create(context: Context): FlickrDB {

            val databaseBuilder = Room.databaseBuilder(context, FlickrDB::class.java, "flickr.db")
            return databaseBuilder
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }
    }

    abstract fun posts(): FlickrDao
}