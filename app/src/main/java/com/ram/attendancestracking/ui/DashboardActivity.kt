package com.ram.attendancestracking.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ram.attendancestracking.adapter.DashboardAdapter
import com.ram.attendancestracking.databinding.ActivityDashboardBinding
import com.ram.attendancestracking.model.UserAttendanceModel
import com.ram.attendancestracking.utils.Constants
import com.ram.attendancestracking.utils.OnAttendanceClick
import com.ram.attendancestracking.viewmodel.DashboardViewModel

class DashboardActivity : AppCompatActivity(), OnAttendanceClick {

    private lateinit var binding: ActivityDashboardBinding
    private val dashboardAdapter = DashboardAdapter(this)

    private val dashboardViewModel by lazy {
        ViewModelProvider(this)[DashboardViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        observerAttendanceData()

    }


    private fun initRecyclerView() {
        binding.rvAttendanceList.adapter = dashboardAdapter
    }

    private fun observerAttendanceData() {

        dashboardViewModel.attendanceRecord.observe(this) {
            it.let {
                dashboardAdapter.submitList(it)
            }
        }
    }

    override fun onClick(userAttendanceModel: UserAttendanceModel) {
        Log.d(Constants.TAG, "" + userAttendanceModel)
        val intent = Intent(this, AttendanceDetailsActivity::class.java)
        intent.putExtra("Data", userAttendanceModel)
        startActivity(intent)

    }
}