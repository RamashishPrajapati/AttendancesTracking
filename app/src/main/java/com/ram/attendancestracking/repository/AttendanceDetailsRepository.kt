package com.ram.attendancestracking.repository

import com.ram.attendancestracking.database.AttendanceDatabase
import com.ram.attendancestracking.model.UserLocation

class AttendanceDetailsRepository(database: AttendanceDatabase) {

    private val database = database

    fun getAttendanceDetails(date: String): List<UserLocation> {
        return database.userLocationDao().getUserLocation(date)
    }
}