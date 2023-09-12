package com.ram.attendancestracking.database

import android.content.Context
import android.content.res.Resources
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ram.attendancestracking.R
import com.ram.attendancestracking.dao.CompanyDao
import com.ram.attendancestracking.dao.UserAttendanceDao
import com.ram.attendancestracking.dao.UserLocationDao
import com.ram.attendancestracking.model.CompanyLocation
import com.ram.attendancestracking.model.UserAttendanceModel
import com.ram.attendancestracking.model.UserLocation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(
    entities = [UserLocation::class, CompanyLocation::class, UserAttendanceModel::class],
    version = 3,
    exportSchema = false
)
abstract class AttendanceDatabase : RoomDatabase() {

    abstract fun userLocationDao(): UserLocationDao
    abstract fun companyDao(): CompanyDao
    abstract fun userAttendanceDao(): UserAttendanceDao

    companion object {
        @Volatile
        private var INSTANCE: AttendanceDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope,
            resources: Resources
        ): AttendanceDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AttendanceDatabase::class.java, "AttendanceDatabase"
                )
                    .addCallback(CompanyDatabaseCallback(scope, resources))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }

    }

    private class CompanyDatabaseCallback(
        private val scope: CoroutineScope,
        private val resources: Resources
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE.let { database ->
                scope.launch(Dispatchers.IO) {
                    var companyDao = database!!.companyDao()
                    prePopulateDatabase(companyDao)
                }
            }
        }

        /*prePopulating database, on creation of database inseting all the record
        from companydetails.json file which is in raw folder of app*/
        private suspend fun prePopulateDatabase(companyDao: CompanyDao) {
            val jsonString = resources.openRawResource(R.raw.companydetails).bufferedReader().use {
                it.readText()
            }
            val typeToken = object : TypeToken<List<CompanyLocation>>() {}.type
            val companyDetails = Gson().fromJson<List<CompanyLocation>>(jsonString, typeToken)
            companyDao.addCompanyDetails(companyDetails)
        }
    }
}