package com.example.scheduler.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.scheduler.model.Activity
import com.example.scheduler.model.Category
import com.example.scheduler.model.DayWithPoints
import com.example.scheduler.repository.Repository
import kotlin.math.floor

class ShowProgressViewModel(private val repository: Repository) : ViewModel() {

    private val daysWithActivities = repository.getDayWithActivitiesForWeek()
    val daysWithPoints: MutableLiveData<List<DayWithPoints>> =
        Transformations.map(daysWithActivities) { dayList ->
            dayList.map { DayWithPoints(
                it.day.dayDate.run { removeRange(length - 5, length) },
                calculateTotalProgress(it.activities)) }
        } as MutableLiveData<List<DayWithPoints>>

    fun calculateProgress(category: Category? = null) {
        daysWithPoints.value = daysWithActivities.value?.map { day ->
            DayWithPoints(day.day.dayDate.run { removeRange(length - 5, length) },
                category?.let {
                    calculateProgressByCategory(day.activities, it)
                } ?: calculateTotalProgress(day.activities))
        }
    }

    private fun calculateTotalProgress(listOfDays: List<Activity>) =
        listOfDays.sumOf { it.points * it.completion / 100 }

    private fun calculateProgressByCategory(activities: List<Activity>, category: Category) =
        activities.filter { activity -> activity.category == category }
            .sumOf { activity -> floor((activity.points * activity.completion / 100).toDouble()).toInt() }
}