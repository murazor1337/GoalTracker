package com.example.scheduler.repository

import android.content.Context
import com.example.scheduler.model.Activity
import com.example.scheduler.model.Category
import com.example.scheduler.model.Day
import com.example.scheduler.model.Sphere
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository {

    var spheresList: MutableList<Sphere> = mutableListOf()

    companion object {
        private lateinit var daysDao: DaysDao
        private lateinit var backupManager: BackupManager
        @Volatile
        private var instance: Repository? = null
        fun getInstance(context: Context): Repository {
            instance = if (instance == null) {
                daysDao = DaysDatabase.getInstance(context).daysDao
                backupManager = BackupManager(context.applicationContext)
                Repository()
            } else instance
            return instance as Repository
        }
    }

    suspend fun insertDay(day: Day) = daysDao.insertDay(day)

    suspend fun deleteDay(day: Day) = daysDao.deleteDay(day)

    suspend fun setDayAdded(day: Day) = daysDao.setDayAdded(day)

    suspend fun deleteAllActivitiesForDay(date: String) = daysDao.deleteAllActivitiesForDay(date)

    fun getDayWithActivities(date: String) = daysDao.getDayWithActivities(date)

    fun getActivitiesByCategory(category: Category) = daysDao.getActivitiesByCategory(category)

    fun getDayWithActivitiesForWeek() = daysDao.getDayWithActivitiesForWeek()

    suspend fun insertActivity(activity: Activity) = daysDao.insertActivity(activity)

    suspend fun deleteActivity(activity: Activity) = daysDao.deleteActivity(activity)

    suspend fun deleteProgress() {
        daysDao.deleteProgress()
        spheresList.forEach { sphere ->
            sphere.progress = 0
            sphere.level = 1
        }
    }

    suspend fun createProgress(progressName: String?) {
        val daysWithActivities = withContext(Dispatchers.IO) { daysDao.getAllDaysWithActivities() }
        backupManager.createProgress(progressName, daysWithActivities, spheresList)
        //deleteProgress()
    }

    suspend fun getProgressList() = backupManager.getProgressList()

    suspend fun updateActivity(activity: Activity) = daysDao.updateActivity(activity)
}