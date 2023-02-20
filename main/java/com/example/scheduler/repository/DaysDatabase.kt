package com.example.scheduler.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.scheduler.model.Activity
import com.example.scheduler.model.Day

@Database(entities = [Day::class, Activity::class], version = 1)
abstract class DaysDatabase: RoomDatabase() {

    abstract val daysDao: DaysDao

    companion object {
        @Volatile
        private var INSTANCE: DaysDatabase? = null

        fun getInstance(context: Context): DaysDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(context.applicationContext,
                DaysDatabase::class.java, "days_db").build().also { dataBase ->
                    INSTANCE = dataBase
                }
            }
        }
    }
}