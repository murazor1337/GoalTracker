package com.example.scheduler.model

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

data class DayWithActivities(
    @Embedded val day: Day,
    @Relation(parentColumn = "dayDate", entityColumn = "date")
    val activities: List<Activity>
): Serializable