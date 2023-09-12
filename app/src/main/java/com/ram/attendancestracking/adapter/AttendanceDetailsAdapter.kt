package com.ram.attendancestracking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ram.attendancestracking.databinding.ItemDetailsAttendanceBinding
import com.ram.attendancestracking.model.UserLocation

class AttendanceDetailsAdapter :
    ListAdapter<UserLocation, AttendanceDetailsAdapter.ItemViewHolder>(
        AttendanceDetailsDiffUtilDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        return ItemViewHolder(
            ItemDetailsAttendanceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemViewHolder(val binding: ItemDetailsAttendanceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserLocation) = with(itemView) {
            binding.tvTime.text = "Time " + item.time
            binding.tvLatitude.text = "Lat " + item.latitude
            binding.tvLongitude.text = "Lon " + item.longitude

        }

    }


}


class AttendanceDetailsDiffUtilDiffCallback : DiffUtil.ItemCallback<UserLocation>() {
    override fun areItemsTheSame(
        oldItem: UserLocation,
        newItem: UserLocation
    ): Boolean {
        return oldItem.time == newItem.time
    }

    override fun areContentsTheSame(
        oldItem: UserLocation,
        newItem: UserLocation
    ): Boolean {
        return oldItem.time == newItem.time
    }

}
