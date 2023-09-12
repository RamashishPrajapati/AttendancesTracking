package com.ram.attendancestracking.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ram.attendancestracking.adapter.AttendanceDetailsAdapter
import com.ram.attendancestracking.databinding.ActivityAttendanceDetailsBinding
import com.ram.attendancestracking.model.UserAttendanceModel
import com.ram.attendancestracking.viewmodel.AttendanceDetailsViewModel

class AttendanceDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAttendanceDetailsBinding
    private val attendanceDetailsAdapter = AttendanceDetailsAdapter()
    private val attendanceViewModel by lazy {
        ViewModelProvider(this)[AttendanceDetailsViewModel::class.java]
    }
    private lateinit var data: UserAttendanceModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()
        initRecyclerView()
        observerAttendanceDetails()
    }

    private fun initListener() {
        data = intent.getSerializableExtra("Data") as UserAttendanceModel
        binding.tvAttendanceDetails.text =
            "${data.companyname}   Start Time ${data.starttime}  End Time ${data.endtime}"
    }

    private fun initRecyclerView() {
        binding.rvAttendanceDetailList.adapter = attendanceDetailsAdapter
    }

    private fun observerAttendanceDetails() {

        attendanceViewModel.getAttendanceRecord(data.date)

        attendanceViewModel.attendanceDetails.observe(this) {
            it.let {
                attendanceDetailsAdapter.submitList(it)
            }
        }
    }


}