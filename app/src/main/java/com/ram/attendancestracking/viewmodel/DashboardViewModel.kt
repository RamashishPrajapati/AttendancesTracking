package com.ram.attendancestracking.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ram.attendancestracking.database.AttendanceDatabase

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val database =
        AttendanceDatabase.getDatabase(application, viewModelScope, application.resources)

    val attendanceRecord = database.userAttendanceDao().getUserAttendance()
}