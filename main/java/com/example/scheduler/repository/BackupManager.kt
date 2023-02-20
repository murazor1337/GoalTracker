package com.example.scheduler.repository

import android.content.Context
import com.example.scheduler.model.DayWithActivities
import com.example.scheduler.model.Progress
import com.example.scheduler.model.Sphere
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream

class BackupManager(private val context: Context) {

    suspend fun getProgressList(): List<String> = context.filesDir.
        walk().toList().map { file -> file.name }.
        filter { filename -> filename.substring(filename.length - 4, filename.length) == ".txt" }.
            map { filename -> filename.substringBefore(".") }

    suspend fun createProgress(
        progressName: String?,
        daysList: List<DayWithActivities>,
        spheresList: List<Sphere>
    ) {
        val progress = progressName?.let { Progress(it, daysList, spheresList) }
        val progressFile = File(context.filesDir, progressName)
        val outputStream = FileOutputStream(progressFile)
        ObjectOutputStream(outputStream).writeObject(progress)
    }
}