package com.ram.attendancestracking.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ram.attendancestracking.model.CompanyLocation

@Dao
interface CompanyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCompanyDetails(companyLocation: List<CompanyLocation>)

    @Query("SELECT * FROM CompanyLocation")
    fun getCompanyDetails(): LiveData<List<CompanyLocation>>

}