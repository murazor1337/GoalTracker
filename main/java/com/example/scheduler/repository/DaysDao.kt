package com.example.scheduler.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.scheduler.model.Activity
import com.example.scheduler.model.Category
import com.example.scheduler.model.Day
import com.example.scheduler.model.DayWithActivities

@Dao
interface DaysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDay(day: Day)

    @Delete
    suspend fun deleteDay(day: Day)

    @Update
    suspend fun setDayAdded(day: Day)

    @Query("DELETE FROM activity WHERE date = :date")
    suspend fun deleteAllActivities(date: String)

    @Transaction
    suspend fun deleteAllActivitiesForDay(date: String) {
        deleteAllActivities(date)
        deleteDay(Day(date))
    }

    @Transaction
    @Query("SELECT * FROM day WHERE dayDate = :date")
    fun getDayWithActivities(date: String): LiveData<List<DayWithActivities>>

    @Transaction
    @Query("SELECT * FROM day WHERE added ORDER BY dayDate DESC LIMIT 7")
    fun getDayWithActivitiesForWeek(): LiveData<List<DayWithActivities>>

    @Transaction
    @Query("SELECT * FROM day")
    suspend fun getAllDaysWithActivities(): List<DayWithActivities>

    @Query("SELECT * FROM activity WHERE category = :category")
    fun getActivitiesByCategory(category: Category): LiveData<List<Activity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: Activity)

    @Delete
    suspend fun deleteActivity(activity: Activity)

    @Query("DELETE FROM day")
    suspend fun deleteAllDays()

    @Query("DELETE FROM activity")
    suspend fun deleteAllActivities()

    @Transaction
    suspend fun deleteProgress() {
        deleteAllActivities()
        deleteAllDays()
    }

    @Update
    suspend fun updateActivity(activity: Activity)
}