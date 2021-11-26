package com.example.flo_clone

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [Song::class, User::class, Like::class], version = 1)
abstract class SongDatabase : RoomDatabase() {
    abstract fun SongDao() : SongDao
    abstract fun UserDao() : UserDao

    companion object {
        private var instance : SongDatabase? = null

        @InternalCoroutinesApi
        @Synchronized
        fun getInstance(context : Context) : SongDatabase? {
            if (instance == null) {
                synchronized(SongDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "user-database" // 다른 데이터 베이스랑 이름겹치면 꼬임
                    ).allowMainThreadQueries().build()
                }
            }
            return instance
        }
    }
}