package com.ram.attendancestracking.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.ram.attendancestracking.R
import com.ram.attendancestracking.databinding.ActivityMainBinding
import com.ram.attendancestracking.locationUtils.LocationService
import com.ram.attendancestracking.model.CompanyLocation
import com.ram.attendancestracking.model.UserAttendanceModel
import com.ram.attendancestracking.utils.CGlobal_lib
import com.ram.attendancestracking.utils.Constants
import com.ram.attendancestracking.utils.PermissionUtils
import com.ram.attendancestracking.utils.Status
import com.ram.attendancestracking.viewmodel.CompanyViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val companyViewModel by lazy {
        ViewModelProvider(this)[CompanyViewModel::class.java]
    }

    private var attendanceOptionList = ArrayList<CompanyLocation>()
    private lateinit var selectCompany: CompanyLocation
    private var currentLatitude: String = ""
    private var currentLongitude: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermission()
        observerCompanyDetails()
        observerAttendanceData()
        initListener()
    }

    private fun initListener() {

        binding.spAttendance.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selectCompany = if (attendanceOptionList[position] != attendanceOptionList[0]) {
                    attendanceOptionList[position]
                } else {
                    attendanceOptionList[position]
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        binding.btnStart.setOnClickListener {
            companyViewModel.markAttendance(
                UserAttendanceModel(
                    binding.tvTodaysDate.text.toString().trim(),
                    binding.tvCurrentTime.text.toString().trim(),
                    selectCompany.companyname,
                    currentLatitude,
                    currentLongitude,
                    startDay = true,
                    endDay = false
                ), selectCompany
            )
        }

        binding.btnEnd.setOnClickListener {
            companyViewModel.markAttendance(
                UserAttendanceModel(
                    binding.tvTodaysDate.text.toString().trim(),
                    binding.tvCurrentTime.text.toString().trim(),
                    selectCompany.companyname,
                    currentLatitude,
                    currentLongitude,
                    startDay = false,
                    endDay = true
                ), selectCompany
            )
        }

        binding.extFabDashboard.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

    }

    private fun checkPermission() {
        if (PermissionUtils.isAccessFineLocationGranted(this)) {
            if (!PermissionUtils.isLocationEnabled(this)) {
                PermissionUtils.showGPSNotEnabledDialog(this)
            } else {
                setUpLocationListener()
            }
        } else {
            PermissionUtils.requestAccessFineLocationPermission(
                this, Constants.LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun startService() {

        Intent(this, LocationService::class.java).apply {
            action = LocationService.ACTION_START
            startService(this)
        }

    }


    private fun stopService() {

        Intent(this, LocationService::class.java).apply {
            action = LocationService.ACTION_START
            stopService(this)
        }

    }

    private fun observerCompanyDetails() {
        companyViewModel.companyDetails.observe(this) {
            it?.let {
                Log.d(Constants.TAG, "" + it)
                if (attendanceOptionList.isNotEmpty()) attendanceOptionList.clear()

                attendanceOptionList.add(CompanyLocation(0, "Select Company", "", ""))
                attendanceOptionList.addAll(it)
                setSpinnerData()
            }
        }
    }

    private fun observerAttendanceData() {
        companyViewModel.markAttendanceLiveData.observe(this) { attendanceStatus ->
            attendanceStatus.let {
                when (it.status) {
                    Status.SUCCESS -> {
                        if (it.message == getString(R.string.start_day_mark_successfully)) {
                            startService()
                        } else if (it.message == getString(R.string.end_day_mark_successfully)) {
                            stopService()
                        }
                        CGlobal_lib.getInstance(this)?.showMessage(it.message!!)
                    }

                    Status.ERROR -> {
                        CGlobal_lib.getInstance(this)?.showMessage(it.message!!)
                    }

                    Status.LOADING -> {

                    }
                }
            }

        }
    }

    private fun setSpinnerData() {
        binding.tvTodaysDate.text = CGlobal_lib.getInstance(this)?.getCurrentDate()

        val cAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item, attendanceOptionList
        )
        binding.spAttendance.adapter = cAdapter
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constants.LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }

                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    Toast.makeText(
                        this, getString(R.string.location_permission_not_granted), Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            PermissionUtils.requestAccessFineLocationPermission(
                this, Constants.LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                    return CancellationTokenSource().token
                }

                override fun isCancellationRequested(): Boolean {
                    return false
                }

            }).addOnSuccessListener { location ->
            if (location != null) {
                currentLatitude = location.latitude.toString()
                currentLongitude = location.longitude.toString()
            }
        }
    }

}