package com.ram.attendancestracking.utils

import com.ram.attendancestracking.model.UserAttendanceModel

interface OnAttendanceClick {
    fun onClick(userAttendanceModel: UserAttendanceModel)
}