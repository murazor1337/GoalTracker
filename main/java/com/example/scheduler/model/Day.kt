package com.example.scheduler.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Day(
    @PrimaryKey(autoGenerate = false) val dayDate: String,
    var added: Boolean = false
)