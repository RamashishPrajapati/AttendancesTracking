package com.ram.attendancestracking.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ram.attendancestracking.model.UserAttendanceModel

@Dao
interface UserAttendanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUserAttendance(userAttendance: UserAttendanceModel): Long

    @Query("SELECT EXISTS(SELECT * FROM UserAttendanceModel WHERE date = :date)")
    fun isRecordExist(date: String): Boolean

    @Query("UPDATE UserAttendanceModel SET endDay = :endDay, endtime = :endTime, elatitude = :eLatitude, elongitude =:eLongitude  WHERE date = :date")
    fun updateUserAttendance(
        date: String,
        endTime: String,
        eLatitude: String,
        eLongitude: String,
        endDay: Boolean
    ): Int

    @Query("SELECT * FROM UserAttendanceModel")
    fun getUserAttendance(): LiveData<List<UserAttendanceModel>>


}