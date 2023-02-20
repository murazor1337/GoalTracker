package com.example.scheduler.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scheduler.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProgressViewModel(private val repository: Repository) : ViewModel() {

    val progress = getProgressList()

    private fun getProgressList(): MutableList<String> {
        var result = mutableListOf<String>()
        viewModelScope.launch {
            result = withContext(Dispatchers.IO) { repository.getProgressList() as MutableList<String> }
        }
        return result
    }
}