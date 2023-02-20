package com.example.scheduler.model

import android.graphics.drawable.Drawable
import kotlinx.serialization.Transient
import java.io.Serializable

data class Sphere(
    val name: String,
    var progress: Int = 0,
    var level: Int = 1,
    @Transient val image: Drawable? = null
): Serializable