package com.example.scheduler.model

import java.io.Serializable

data class Progress(
    val name: String,
    val daysList: List<DayWithActivities>,
    val spheresList: List<Sphere>
): Serializable