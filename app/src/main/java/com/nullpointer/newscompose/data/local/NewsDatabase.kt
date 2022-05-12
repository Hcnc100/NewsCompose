package com.nullpointer.newscompose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nullpointer.newscompose.models.NewsDB

@Database(
    entities = [NewsDB::class],
    version = 1,
    exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun getNewDao(): NewsDao
}