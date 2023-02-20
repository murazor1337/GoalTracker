package com.example.scheduler.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.scheduler.model.Activity
import com.example.scheduler.model.Category
import com.example.scheduler.repository.Repository

class ActivitiesByCategoryViewModel(private val repository: Repository) : ViewModel() {

    lateinit var activitiesList: LiveData<List<Activity>>

    fun getActivitiesByCategory(category: Category) {
        activitiesList = repository.getActivitiesByCategory(category)
    }
}