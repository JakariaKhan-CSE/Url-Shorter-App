package com.example.glyde.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val originalUrl: String,
    val shortUrl: String,
    val timestamp: Long = System.currentTimeMillis()
)
