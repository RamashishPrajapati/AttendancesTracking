package com.ram.attendancestracking.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ram.attendancestracking.R
import com.ram.attendancestracking.database.AttendanceDatabase
import com.ram.attendancestracking.model.CompanyLocation
import com.ram.attendancestracking.model.UserAttendanceModel
import com.ram.attendancestracking.repository.CompanyRepository
import com.ram.attendancestracking.utils.Resource
import com.ram.attendancestracking.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CompanyViewModel(application: Application) : AndroidViewModel(application) {

    private val database =
        AttendanceDatabase.getDatabase(application, viewModelScope, application.resources)

    private val companyRepository = CompanyRepository(database)
    var companyDetails = database.companyDao().getCompanyDetails()

    var markAttendanceLiveData = MutableLiveData<Resource<String>>()


    fun markAttendance(
        userAttendanceModel: UserAttendanceModel,
        companyLocation: CompanyLocation,
    ) {
        if (companyLocation.id == 0) {
            markAttendanceLiveData.postValue(
                Resource(
                    Status.ERROR,
                    null,
                    getApplication<Application>().getString(R.string.please_select_company)
                )
            )
            return
        }
        if (userAttendanceModel.latitude.isEmpty() || userAttendanceModel.longitude.isEmpty()) {
            markAttendanceLiveData.postValue(
                Resource(
                    Status.ERROR,
                    null,
                    getApplication<Application>().getString(R.string.please_check_your_gps_is_enable)
                )
            )
            return
        }

        if (companyLocation.latitude.isNotEmpty() && companyLocation.longitude.isNotEmpty() &&
            userAttendanceModel.latitude.isNotEmpty() && userAttendanceModel.longitude.isNotEmpty()
        ) {
            val cLocation = Location("CompanyLocation")
            cLocation.latitude = companyLocation.latitude.toDouble()
            cLocation.longitude = companyLocation.longitude.toDouble()

            val uLocation = Location("UserLocation")
            uLocation.latitude = userAttendanceModel.latitude.toDouble()
            uLocation.longitude = userAttendanceModel.longitude.toDouble()

            val distance = uLocation.distanceTo(cLocation)

            if (distance > 100) {
                markAttendanceLiveData.postValue(
                    Resource(
                        Status.ERROR,
                        null,
                        getApplication<Application>().getString(R.string.please_be_in_range_of_100m_of_company_to_start_day)
                    )
                )
                return
            }


        }

        viewModelScope.launch(Dispatchers.IO) {
            val isExist = companyRepository.checkAttendance(userAttendanceModel.date)
            if (isExist) {
                if (userAttendanceModel.startDay) {
                    markAttendanceLiveData.postValue(
                        Resource(
                            Status.ERROR,
                            null,
                            getApplication<Application>().getString(R.string.attendance_already_mark_fro_the_day)
                        )
                    )
                } else if (userAttendanceModel.endDay) {
                    val value = companyRepository.updateAttendance(userAttendanceModel)
                    if (value.toInt() != -1) {
                        markAttendanceLiveData.postValue(
                            Resource(
                                Status.SUCCESS,
                                null,
                                getApplication<Application>().getString(R.string.end_day_mark_successfully)
                            )
                        )
                    } else {
                        markAttendanceLiveData.postValue(
                            Resource(
                                Status.ERROR,
                                null,
                                getApplication<Application>().getString(R.string.attendance_not_mark_please_try_again)
                            )
                        )
                    }
                }


            } else {
                if (userAttendanceModel.startDay) {
                    val value = companyRepository.markAttendance(userAttendanceModel)
                    if (value.toInt() != -1) {

                        markAttendanceLiveData.postValue(
                            Resource(
                                Status.SUCCESS,
                                null,
                                getApplication<Application>().getString(R.string.start_day_mark_successfully)
                            )
                        )
                    } else {
                        markAttendanceLiveData.postValue(
                            Resource(
                                Status.ERROR,
                                null,
                                getApplication<Application>().getString(R.string.attendance_not_mark_please_try_again)
                            )
                        )
                    }
                } else if (userAttendanceModel.endDay) {
                    markAttendanceLiveData.postValue(
                        Resource(
                            Status.ERROR,
                            null,
                            getApplication<Application>().getString(R.string.please_mark_start_day)
                        )
                    )
                }

            }
        }
    }

}