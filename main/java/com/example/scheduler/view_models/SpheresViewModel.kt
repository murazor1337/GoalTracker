package com.example.scheduler.view_models

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.scheduler.R
import com.example.scheduler.model.Sphere
import com.example.scheduler.repository.Repository
import kotlinx.coroutines.launch

class SpheresViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences(
        application.getString(R.string.preferences_key), Context.MODE_PRIVATE)

    var isSpheresWasUpdated = false

    private val repository: Repository = Repository.getInstance(application)
    val spheresList: List<Sphere>
    init {
        if (!isSpheresWasUpdated) {
            spheresList = restoreList()
            repository.spheresList = spheresList as MutableList<Sphere>
        } else {
            spheresList = repository.spheresList
        }
    }

    fun saveList() {
        sharedPreferences.edit {
            saveList(spheresList)
            apply()
        }
    }

    private fun restoreList() = sharedPreferences.restoreList()

    fun deleteProgress(progressName: String?, save: Boolean) {
        if (!save) {
            viewModelScope.launch { repository.deleteProgress() }
        } else {
            viewModelScope.launch { repository.createProgress(progressName) }
        }
    }

    private fun SharedPreferences.Editor.saveList(list: List<Sphere>) {
        putInt("health_points", list[0].progress)
        putInt("health_level", list[0].level)
        putInt("socialisation_points", list[1].progress)
        putInt("socialisation_level", list[1].level)
        putInt("career_points", list[2].progress)
        putInt("career_level", list[2].level)
        putInt("education_points", list[3].progress)
        putInt("education_level", list[3].level)
        putInt("self_development_points", list[4].progress)
        putInt("self_development_level", list[4].level)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun SharedPreferences.restoreList() = listOf(
        Sphere("Health", getInt("health_points", 0), getInt("health_level", 1),
            getApplication<Application>().getDrawable(R.drawable.healthcare)),
        Sphere("Socialisation", getInt("socialisation_points", 0), getInt("socialisation_level", 1),
            getApplication<Application>().getDrawable(R.drawable.network)),
        Sphere("Career", getInt("career_points", 0), getInt("career_level", 1),
            getApplication<Application>().getDrawable(R.drawable.work)),
        Sphere("Education", getInt("education_points", 0), getInt("education_level", 1),
            getApplication<Application>().getDrawable(R.drawable.graduation)),
        Sphere("Self development", getInt("self_development_points", 0), getInt("self_development_level", 1),
            getApplication<Application>().getDrawable(R.drawable.improve))
    )
}