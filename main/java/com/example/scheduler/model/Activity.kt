package com.example.scheduler.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Activity(@PrimaryKey(autoGenerate = true) val id: Long, val description: String,
                    val category: Category, val date: String,
                    val points: Int, val completion: Int = 0,
                    val isPlanned: Boolean = true): Serializable {

                        fun pointsToString() = points.toString()
                        fun completionToString() = completion.toString()
                    }