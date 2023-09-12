package com.ram.attendancestracking.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserLocation(
    @PrimaryKey
    val time: String,
    val date: String,
    val latitude: String,
    val longitude: String
)

