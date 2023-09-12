package com.ram.attendancestracking.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ram.attendancestracking.database.AttendanceDatabase
import com.ram.attendancestracking.model.UserLocation
import com.ram.attendancestracking.repository.AttendanceDetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AttendanceDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val database =
        AttendanceDatabase.getDatabase(application, viewModelScope, application.resources)
    private val attendanceDetailRepository = AttendanceDetailsRepository(database)
    var attendanceDetails = MutableLiveData<List<UserLocation>>()


    fun getAttendanceRecord(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            attendanceDetails.postValue(attendanceDetailRepository.getAttendanceDetails(date))
        }
    }

}