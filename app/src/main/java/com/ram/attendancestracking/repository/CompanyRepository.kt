package com.ram.attendancestracking.repository

import androidx.lifecycle.LiveData
import com.ram.attendancestracking.database.AttendanceDatabase
import com.ram.attendancestracking.model.CompanyLocation
import com.ram.attendancestracking.model.UserAttendanceModel

class CompanyRepository(database: AttendanceDatabase) {


    private val database = database

    fun getCompanyDetails(): LiveData<List<CompanyLocation>> {
        return database.companyDao().getCompanyDetails()
    }

    fun checkAttendance(date: String): Boolean {
        return database.userAttendanceDao().isRecordExist(date)
    }

    fun markAttendance(userAttendanceModel: UserAttendanceModel): Long {
        return database.userAttendanceDao().addUserAttendance(userAttendanceModel)
    }

    fun updateAttendance(userAttendanceModel: UserAttendanceModel): Int {
        return database.userAttendanceDao().updateUserAttendance(
                userAttendanceModel.date,
                userAttendanceModel.starttime,
                userAttendanceModel.latitude,
                userAttendanceModel.longitude,
                userAttendanceModel.endDay
            )
    }

}