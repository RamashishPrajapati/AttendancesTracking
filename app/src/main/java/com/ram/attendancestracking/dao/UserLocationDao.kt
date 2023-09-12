package com.ram.attendancestracking.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ram.attendancestracking.model.UserLocation

@Dao
interface UserLocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUserLocation(location: UserLocation)

    @Delete
    fun deleteUserLocation(location: UserLocation)

    @Query("DELETE FROM UserLocation")
    fun deleteLocation()

    @Query("SELECT * FROM UserLocation WHERE  date = :date")
    fun getUserLocation(date: String): List<UserLocation>
}