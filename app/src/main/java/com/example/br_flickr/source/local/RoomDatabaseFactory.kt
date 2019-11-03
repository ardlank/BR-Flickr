package com.example.br_flickr.source.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.br_flickr.model.PhotoObject

//Database schema used by the PhotoRepoDB
@Database(
    entities = [PhotoObject::class],
    version = 1,
    exportSchema = false
)
abstract class RoomDatabaseFactory : RoomDatabase() {
    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: RoomDatabaseFactory? = null

        fun getInstance(context: Context, databaseName: String): RoomDatabaseFactory {
            return instance ?: synchronized(this) {
                instance ?: create(context, databaseName).also { instance = it }
            }
        }

        private fun create(context: Context, databaseName: String): RoomDatabaseFactory {
            val databaseBuilder = Room.databaseBuilder(context, RoomDatabaseFactory::class.java, databaseName)
            return databaseBuilder
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun posts(): FlickrDao
}