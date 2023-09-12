package com.ram.attendancestracking.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CompanyLocation(
    @PrimaryKey val id: Int,
    val companyname: String,
    val latitude: String,
    val longitude: String
) {
    override fun toString(): String {
        return companyname
    }
}
