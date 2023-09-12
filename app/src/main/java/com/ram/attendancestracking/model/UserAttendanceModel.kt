package com.ram.attendancestracking.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class UserAttendanceModel(
    @PrimaryKey val date: String,
    val starttime: String,
    val companyname: String,
    val latitude: String,
    val longitude: String,
    val startDay: Boolean,
    val endDay: Boolean,
    val endtime: String,
    val elatitude: String,
    val elongitude: String
) : Serializable {

    @Ignore
    constructor(
        date: String,
        starttime: String,
        companyname: String,
        latitude: String,
        longitude: String,
        startDay: Boolean,
        endDay: Boolean
    ) : this(date, starttime, companyname, latitude, longitude, startDay, endDay, "", "", "")
}