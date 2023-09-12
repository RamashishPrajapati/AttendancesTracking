package com.ram.attendancestracking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ram.attendancestracking.databinding.ItemAttendanceBinding
import com.ram.attendancestracking.model.UserAttendanceModel
import com.ram.attendancestracking.utils.OnAttendanceClick

class DashboardAdapter(onAttendanceClick: OnAttendanceClick) :
    ListAdapter<UserAttendanceModel, DashboardAdapter.ItemViewHolder>(DashboardDiffUtilDiffCallback()) {

    private val onAttendanceClick = onAttendanceClick

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        return ItemViewHolder(
            ItemAttendanceBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position), onAttendanceClick)
    }

    class ItemViewHolder(val binding: ItemAttendanceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserAttendanceModel, onAttendanceClick: OnAttendanceClick) = with(itemView) {
            binding.tvCompanyName.text = "Company Name " + item.companyname
            binding.tvStartTime.text = "Start Time " + item.starttime
            binding.tvEndTime.text = "End Time " + item.endtime

            binding.llMain.setOnClickListener {
                if (onAttendanceClick != null) {
                    onAttendanceClick.onClick(item)
                }
            }
        }

    }


}


class DashboardDiffUtilDiffCallback : DiffUtil.ItemCallback<UserAttendanceModel>() {
    override fun areItemsTheSame(
        oldItem: UserAttendanceModel, newItem: UserAttendanceModel
    ): Boolean {
        return oldItem.date == newItem.date && oldItem.starttime == newItem.starttime
    }

    override fun areContentsTheSame(
        oldItem: UserAttendanceModel, newItem: UserAttendanceModel
    ): Boolean {
        return oldItem.date == newItem.date && oldItem.starttime == newItem.starttime
    }

}
