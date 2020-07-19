package com.jayashree.wordclock.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "location_table")
data class Location(@PrimaryKey @ColumnInfo(name = "timezone") val timezone: String = "")