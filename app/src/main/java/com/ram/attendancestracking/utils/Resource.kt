package com.ram.attendancestracking.utils


data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?
) {

    companion object {
        fun <T> success(data: T?, msg: String?): Resource<T> {
            return Resource(Status.SUCCESS, data, msg)
        }

        fun <T> error(msg: String?): Resource<T> {
            return Resource(Status.ERROR, null, msg)
        }

        fun <T> loading(msg: String?): Resource<T> {
            return Resource(Status.LOADING, null, msg)
        }
    }
}