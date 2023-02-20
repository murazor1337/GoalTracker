package com.example.scheduler.view_models

import androidx.lifecycle.*
import com.example.scheduler.model.Activity
import com.example.scheduler.model.Category
import com.example.scheduler.model.Day
import com.example.scheduler.model.DayWithActivities
import com.example.scheduler.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import kotlin.math.floor

class DayInfoViewModel(private val daysRepository: Repository): ViewModel() {

    var dayActivities: LiveData<List<DayWithActivities>> = MutableLiveData()
    var isDayAdded: Boolean = false

    fun getDayWithActivities(date: String) {
        dayActivities = daysRepository.getDayWithActivities(date)
    }

    fun validateActivityInfo(activity: Activity) =
        (activity.description.isNotEmpty() && activity.points != 0)

    fun insertActivity(activity: Activity) {
        if (isDayAdded) {
            viewModelScope.launch(Dispatchers.IO) {
                daysRepository.insertActivity(activity)
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                daysRepository.insertDay(Day(activity.date))
                daysRepository.insertActivity(activity)
            }
        }
    }

    fun deleteActivity(activity: Activity, deleteDay: Boolean) = viewModelScope.launch {
        if (deleteDay) {
            daysRepository.deleteActivity(activity)
            daysRepository.deleteDay(Day(activity.date))
        } else daysRepository.deleteActivity(activity)
    }

    fun deleteAllActivitiesForDay(date: String) = viewModelScope.launch(Dispatchers.IO) {
        daysRepository.deleteAllActivitiesForDay(date)
    }

    fun updateActivity(activity: Activity) = viewModelScope.launch {
        daysRepository.updateActivity(activity)
    }

    fun updateSpheresInfo(activities: List<Activity>, addActivity: Boolean) {
        var healthPoints = calculateSumByCategory(activities, Category.Health)
        var socialisationPoints = calculateSumByCategory(activities, Category.Socialisation)
        var careerPoints = calculateSumByCategory(activities, Category.Career)
        var educationPoints = calculateSumByCategory(activities, Category.Education)
        var selfDevelopmentPoints = calculateSumByCategory(activities, Category.SelfDevelopment)
        if (addActivity) {
            viewModelScope.launch {
                daysRepository.setDayAdded(Day(activities.first().date, true))
            }
        } else {
            healthPoints = -healthPoints
            socialisationPoints = -socialisationPoints
            careerPoints = -careerPoints
            educationPoints = -educationPoints
            selfDevelopmentPoints = -selfDevelopmentPoints
        }
        val pointsListByCategory = listOf(healthPoints, socialisationPoints, careerPoints,
        educationPoints, selfDevelopmentPoints)
        pointsListByCategory.forEachIndexed {index, points ->
            daysRepository.spheresList[index].progress += points
            val currentProgress = daysRepository.spheresList[index].progress
            daysRepository.spheresList[index].level = when {
                currentProgress >= 100 -> {
                    daysRepository.spheresList[index].progress = currentProgress - 100
                    daysRepository.spheresList[index].level + 1
                }
                currentProgress < 0 -> {
                    daysRepository.spheresList[index].progress = 100 + currentProgress
                    daysRepository.spheresList[index].level - 1
                }
                else -> daysRepository.spheresList[index].level
            }
        }
    }

    private fun calculateSumByCategory(activities: List<Activity>, category: Category) =
        activities.filter { activity -> activity.category == category }
            .sumOf { activity -> floor((activity.points * activity.completion / 100).toDouble()).toInt() }
}

class DayInfoViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = when(modelClass) {
        DayInfoViewModel::class.java -> DayInfoViewModel(repository) as T
        ActivitiesByCategoryViewModel::class.java -> ActivitiesByCategoryViewModel(repository) as T
        ShowProgressViewModel::class.java -> ShowProgressViewModel(repository) as T
        ProgressViewModel::class.java -> ProgressViewModel(repository) as T
        else -> throw IllegalArgumentException("Unknown ViewModel class")
    }
}